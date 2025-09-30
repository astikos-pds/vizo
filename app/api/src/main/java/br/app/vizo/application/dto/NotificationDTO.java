package br.app.vizo.application.dto;

import br.app.vizo.core.notification.NotificationType;
import br.app.vizo.core.notification.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record NotificationDTO<T extends DomainEvent>(
        UUID id,
        UserDTO recipient,
        NotificationType type,
        T payload,
        boolean read,
        Instant createdAt
) {
}
