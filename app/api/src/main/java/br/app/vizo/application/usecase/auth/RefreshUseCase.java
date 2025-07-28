package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.dto.TokenPairDTO;
import br.app.vizo.application.mapper.RefreshTokenMapper;
import br.app.vizo.application.service.HashService;
import br.app.vizo.application.usecase.UseCase;
import br.app.vizo.application.usecase.auth.request.RefreshRequestDTO;
import br.app.vizo.config.security.JwtService;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;
import br.app.vizo.exception.UnauthorizedException;
import br.app.vizo.infrastructure.persistence.RefreshTokenRepository;
import br.app.vizo.infrastructure.persistence.UserRepository;
import br.app.vizo.infrastructure.persistence.entity.RefreshTokenEntity;
import br.app.vizo.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RefreshUseCase {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenMapper refreshTokenMapper;
    private final HashService hashService;

    public TokenPairDTO execute(RefreshRequestDTO body) {
        if (!this.jwtService.isRefreshTokenValid(body.token())) {
            throw new UnauthorizedException("Invalid refresh token.");
        }

        String document = this.jwtService.getSubjectFromToken(body.token());
        UserEntity userEntity = this.userRepository.findByDocument(document).orElseThrow(
                () -> new UnauthorizedException("Invalid refresh token.")
        );

        String hashedToken = this.hashService.hashToken(body.token());

        this.refreshTokenRepository.findByTokenAndUserId(hashedToken, userEntity.getId()).orElseThrow(
                () -> new UnauthorizedException("Refresh token not recognized, maybe used or expired.")
        );

        this.refreshTokenRepository.deleteByTokenAndUserId(hashedToken, userEntity.getId());

        String newAccessToken = this.jwtService.generateAccessToken(document);
        String newRefreshToken = this.jwtService.generateRefreshToken(document);

        RefreshToken created = this.refreshTokenFactory.create(new UserId(userEntity.getId()), newRefreshToken);
        RefreshTokenEntity refreshTokenEntity = this.refreshTokenMapper.toEntity(created);
        this.refreshTokenRepository.save(refreshTokenEntity);

        return new TokenPairDTO(newAccessToken, newRefreshToken);
    }
}
