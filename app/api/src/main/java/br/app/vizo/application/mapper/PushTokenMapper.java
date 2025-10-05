package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.PushTokenDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.push.PushToken;
import br.app.vizo.infrastructure.persistence.jpa.entity.PushTokenEntity;

@Mapper
public class PushTokenMapper implements RepresentationMapper<PushToken, PushTokenEntity, PushTokenDTO> {

    @Override
    public PushTokenDTO toDto(PushToken pushToken) {
        return new PushTokenDTO(
                pushToken.getId(),
                pushToken.getUserId(),
                pushToken.getToken(),
                pushToken.getPlatform(),
                pushToken.getCreatedAt(),
                pushToken.getLastUsedAt()
        );
    }

    @Override
    public PushTokenEntity toEntity(PushToken pushToken) {
        return new PushTokenEntity(
                pushToken.getId(),
                pushToken.getUserId(),
                pushToken.getToken(),
                pushToken.getPlatform(),
                pushToken.getCreatedAt(),
                pushToken.getLastUsedAt()
        );
    }

    @Override
    public PushToken toModel(PushTokenEntity entity) {
        return new PushToken(
                entity.getId(),
                new UserId(entity.getUserId()),
                entity.getToken(),
                entity.getPlatform(),
                entity.getCreatedAt(),
                entity.getLastUsedAt()
        );
    }
}
