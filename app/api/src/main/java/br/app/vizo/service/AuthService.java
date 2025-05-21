package br.app.vizo.service;

import br.app.vizo.config.security.JwtService;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.domain.token.RefreshToken;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.User;
import br.app.vizo.domain.user.dto.CitizenDTO;
import br.app.vizo.mapper.CitizenMapper;
import br.app.vizo.repository.CitizenRepository;
import br.app.vizo.repository.RefreshTokenRepository;
import br.app.vizo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CitizenMapper citizenMapper;

    public AuthService(
            UserRepository userRepository,
            CitizenRepository citizenRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CitizenMapper citizenMapper
    ) {
        this.userRepository = userRepository;
        this.citizenRepository = citizenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.citizenMapper = citizenMapper;
    }

    public CitizenDTO registerCitizen(String document, String email, String password, String name) {
        this.citizenRepository.findByDocument(document).ifPresent(
                c -> {
                    throw new RuntimeException("Invalid credentials.");
                }
        );

        Citizen citizen = new Citizen();
        citizen.setDocument(document);
        citizen.setEmail(email);
        citizen.setName(name);

        String hashedPassword = this.passwordEncoder.encode(password);
        citizen.setPassword(hashedPassword);

        return this.citizenMapper.toDto(this.citizenRepository.save(citizen));
    }

    public TokenPairDTO login(String document, String password) {
        User user = this.userRepository.findByDocument(document).orElseThrow(
                () -> new RuntimeException("Invalid credentials")
        );

        boolean passwordMatches = this.passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) throw new RuntimeException("Invalid credentials");

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
            throw new RuntimeException("Invalid refresh token.");
        }

        String document = this.jwtService.getSubjectFromToken(refreshToken);
        User user = this.userRepository.findByDocument(document).orElseThrow(
                () -> new RuntimeException("Invalid refresh token.")
        );

        String hashedToken = this.hashToken(refreshToken);

        this.refreshTokenRepository.findByTokenAndUserId(hashedToken, user.getId()).orElseThrow(
                () -> new RuntimeException("Refresh token not recognized, maybe used or expired.")
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
}
