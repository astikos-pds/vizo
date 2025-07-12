package br.app.vizo.service.auth;

import br.app.vizo.config.security.JwtService;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.controller.request.EmailRequestDTO;
import br.app.vizo.controller.request.VerificationCodeRequestDTO;
import br.app.vizo.controller.response.EmailVerificationDTO;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.domain.token.RefreshToken;
import br.app.vizo.domain.user.User;
import br.app.vizo.domain.verification.EmailVerificationRequest;
import br.app.vizo.exception.http.ConflictException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.exception.http.UnauthorizedException;
import br.app.vizo.exception.http.UnprocessableEntityException;
import br.app.vizo.repository.*;
import br.app.vizo.service.EmailService;
import br.app.vizo.util.CodeGenerator;
import br.app.vizo.util.DateUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationRequestRepository emailVerificationRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            EmailVerificationRequestRepository emailVerificationRequestRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailVerificationRequestRepository = emailVerificationRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public TokenPairDTO login(String document, String password) {
        User user = this.userRepository.findByDocument(document).orElseThrow(
                () -> new UnauthorizedException("Invalid credentials")
        );

        boolean passwordMatches = this.passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) throw new UnauthorizedException("Invalid credentials");

        var authConfig = new UsernamePasswordAuthenticationToken(document, password);
        Authentication authentication = this.authenticationManager.authenticate(authConfig);
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = this.jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = this.jwtService.generateRefreshToken(userDetails.getUsername());

        this.saveRefreshToken(refreshToken, user.getId());

        return new TokenPairDTO(accessToken, refreshToken);
    }

    @Transactional
    public TokenPairDTO refresh(String refreshToken) {
        if (!this.jwtService.isRefreshTokenValid(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token.");
        }

        String document = this.jwtService.getSubjectFromToken(refreshToken);
        User user = this.userRepository.findByDocument(document).orElseThrow(
                () -> new UnauthorizedException("Invalid refresh token.")
        );

        String hashedToken = this.hashToken(refreshToken);

        this.refreshTokenRepository.findByTokenAndUserId(hashedToken, user.getId()).orElseThrow(
                () -> new UnauthorizedException("Refresh token not recognized, maybe used or expired.")
        );

        this.refreshTokenRepository.deleteByTokenAndUserId(hashedToken, user.getId());

        String newAccessToken = this.jwtService.generateAccessToken(document);
        String newRefreshToken = this.jwtService.generateRefreshToken(document);

        this.saveRefreshToken(newRefreshToken, user.getId());

        return new TokenPairDTO(newAccessToken, newRefreshToken);
    }

    private void saveRefreshToken(String token, UUID userId) {
        this.refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(this.hashToken(token))
                        .userId(userId)
                        .expiresAt(this.jwtService.refreshTokenExpiresAt())
                        .createdAt(Instant.now())
                        .build()
        );
    }

    private String hashToken(String token) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e) {
            return null;
        }
        var bytes = digest.digest(token.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static int VERIFICATION_CODE_EXPIRATION_IN_MINUTES = 10;

    public EmailVerificationDTO createVerificationRequest(EmailRequestDTO body) {
        EmailVerificationRequest emailVerificationRequest = this.emailVerificationRequestRepository
                .findByEmail(body.email())
                .orElseGet(EmailVerificationRequest::new);

        if (emailVerificationRequest.isVerified()) {
            throw new ConflictException("E-mail is already verified.");
        }

        emailVerificationRequest.setEmail(body.email());
        emailVerificationRequest.setCode(CodeGenerator.generate());
        emailVerificationRequest.setVerified(false);

        final int CODE_EXPIRATION_IN_SECONDS = VERIFICATION_CODE_EXPIRATION_IN_MINUTES * 60;
        emailVerificationRequest.setExpiresAt(DateUtil.now().plusSeconds(CODE_EXPIRATION_IN_SECONDS));

        emailService.sendVerificationCode(
                emailVerificationRequest.getEmail(),
                emailVerificationRequest.getCode(),
                VERIFICATION_CODE_EXPIRATION_IN_MINUTES
        );

        return EmailVerificationDTO.of(
                this.emailVerificationRequestRepository.save(emailVerificationRequest)
        );
    }

    public void verifyEmail(UUID verificationRequestId, VerificationCodeRequestDTO body) {
        EmailVerificationRequest emailVerificationRequest = this.emailVerificationRequestRepository
                .findById(verificationRequestId)
                .orElseThrow(() -> new NotFoundException("Verification request not found."));

        if (emailVerificationRequest.isExpired()) {
            throw new UnprocessableEntityException("Verification code expired, please try again.");
        }

        if (!emailVerificationRequest.getCode().equals(body.code())) {
            throw new UnprocessableEntityException("The codes don't matches.");
        }

        emailVerificationRequest.setVerified(true);

        this.emailVerificationRequestRepository.save(emailVerificationRequest);
    }
}
