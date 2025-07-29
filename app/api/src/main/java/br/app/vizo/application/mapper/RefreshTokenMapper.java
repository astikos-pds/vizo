package br.app.vizo.application.mapper;

import br.app.vizo.application.mapper.base.EntityMapper;
import br.app.vizo.application.Mapper;
import br.app.vizo.application.mapper.base.ModelMapper;
import br.app.vizo.core.shared.ExpirationTimestamp;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.infrastructure.persistence.jpa.entity.RefreshTokenEntity;

@Mapper
public class RefreshTokenMapper implements
        EntityMapper<RefreshToken, RefreshTokenEntity>,
        ModelMapper<RefreshTokenEntity, RefreshToken>
{
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

    @Override
    public RefreshToken toModel(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                new UserId(entity.getUserId()),
                entity.getToken(),
                new ExpirationTimestamp(entity.getExpiresAt()),
                entity.getCreatedAt()
        );
    }
}
