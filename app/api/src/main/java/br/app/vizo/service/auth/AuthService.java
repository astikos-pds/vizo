package br.app.vizo.service.auth;

import br.app.vizo.config.security.JwtService;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.controller.request.RegisterRequestDTO;
import br.app.vizo.controller.response.OfficialDTO;
import br.app.vizo.controller.response.TokenPairDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.token.RefreshToken;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;
import br.app.vizo.domain.user.User;
import br.app.vizo.controller.response.CitizenDTO;
import br.app.vizo.exception.http.UnauthorizedException;
import br.app.vizo.mapper.CitizenMapper;
import br.app.vizo.mapper.OfficialMapper;
import br.app.vizo.repository.*;
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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            CitizenRepository citizenRepository,
            OfficialRepository officialRepository,
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            OfficialMapper officialMapper
    ) {
        this.userRepository = userRepository;
        this.citizenRepository = citizenRepository;
        this.officialRepository = officialRepository;
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.officialMapper = officialMapper;
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
}
