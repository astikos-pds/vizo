package br.app.vizo.application.mapper;

import br.app.vizo.application.mapper.base.EntityMapper;
import br.app.vizo.application.Mapper;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.infrastructure.persistence.jpa.entity.RefreshTokenEntity;

@Mapper
public class RefreshTokenMapper implements EntityMapper<RefreshToken, RefreshTokenEntity> {

    @Override
    public RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return new RefreshTokenEntity(
                refreshToken.getId(),
                refreshToken.getUserId(),
                refreshToken.getToken(),
                refreshToken.getExpiresAt(),
                refreshToken.getCreatedAt()
        );
    }
}
