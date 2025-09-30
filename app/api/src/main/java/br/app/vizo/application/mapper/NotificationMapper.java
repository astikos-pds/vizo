package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.NotificationDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.notification.Notification;
import br.app.vizo.core.notification.NotificationType;
import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.notification.event.NewProblemEvent;
import br.app.vizo.infrastructure.persistence.jpa.entity.NotificationEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper
public class NotificationMapper
        implements RepresentationMapper<Notification<?>, NotificationEntity, NotificationDTO<?>> {

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public NotificationMapper(UserMapper userMapper, ObjectMapper objectMapper) {
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public NotificationDTO<? extends DomainEvent> toDto(Notification<? extends DomainEvent> notification) {
        return new NotificationDTO<>(
                notification.getId(),
                this.userMapper.toDto(notification.getRecipient()),
                notification.getType(),
                notification.getPayload(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    @Override
    public NotificationEntity toEntity(Notification<? extends DomainEvent> notification) {
        return new NotificationEntity(
                notification.getId(),
                this.userMapper.toEntity(notification.getRecipient()),
                notification.getType(),
                this.objectMapper.convertValue(notification.getPayload(), new TypeReference<>() {}),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    @Override
    public Notification<DomainEvent> toModel(NotificationEntity entity) {
        DomainEvent payload = switch (entity.getType()) {
            case NotificationType.NEW_PROBLEM -> this.objectMapper
                    .convertValue(entity.getPayload(), NewProblemEvent.class);
        };

        return new Notification<>(
                entity.getId(),
                this.userMapper.toModel(entity.getRecipient()),
                entity.getType(),
                payload,
                entity.isRead(),
                entity.getCreatedAt()
        );
    }
}
