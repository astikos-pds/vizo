package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.TokenPairDTO;
import br.app.vizo.application.exception.auth.InvalidRefreshTokenException;
import br.app.vizo.application.exception.auth.RefreshTokenNotRecognizedException;
import br.app.vizo.application.service.HashService;
import br.app.vizo.application.usecase.auth.request.RefreshRequestDTO;
import br.app.vizo.config.security.JwtService;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.UserRepository;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;
import br.app.vizo.core.user.token.RefreshTokenRepository;
import br.app.vizo.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class RefreshUseCase {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final HashService hashService;

    @Transactional
    public TokenPairDTO execute(RefreshRequestDTO body) {
        if (!this.jwtService.isRefreshTokenValid(body.token())) {
            throw new UnauthorizedException("Invalid refresh token.");
        }

        String document = this.jwtService.getSubjectFromToken(body.token());
        User user = this.userRepository.findByDocument(document).orElseThrow(InvalidRefreshTokenException::new);

        String hashedToken = this.hashService.hashToken(body.token());

        Optional<RefreshToken> refreshToken = this.refreshTokenRepository
                .findByTokenAndUserId(hashedToken, user.getId());

        if (refreshToken.isEmpty() || refreshToken.get().isExpired()) {
            throw new RefreshTokenNotRecognizedException();
        }

        this.refreshTokenRepository.deleteByTokenAndUserId(hashedToken, user.getId());

        String newAccessToken = this.jwtService.generateAccessToken(document);
        String newRefreshToken = this.jwtService.generateRefreshToken(document);

        RefreshToken created = this.refreshTokenFactory.create(new UserId(user.getId()), newRefreshToken);
        this.refreshTokenRepository.save(created);

        return new TokenPairDTO(newAccessToken, newRefreshToken);
    }
}
