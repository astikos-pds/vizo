package br.app.vizo.core.notification;

import br.app.vizo.core.notification.event.DomainEvent;
import br.app.vizo.core.notification.exception.InvalidNotificationException;
import br.app.vizo.core.user.User;

import java.time.Instant;
import java.util.UUID;

public class Notification<T extends DomainEvent> {

    private final UUID id;
    private final User recipient;
    private final NotificationType type;
    private final T payload;
    private boolean read;
    private final Instant createdAt;

    public Notification(UUID id, User recipient, NotificationType type, T payload, boolean read, Instant createdAt) {
        if (id == null || recipient == null || type == null || payload == null || createdAt == null) {
            throw new InvalidNotificationException();
        }

        this.id = id;
        this.recipient = recipient;
        this.type = type;
        this.payload = payload;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Notification(User recipient, NotificationType type, T payload) {
        this(UUID.randomUUID(), recipient, type, payload, false, Instant.now());
    }

    public void markAsRead() {
        this.read = true;
    }

    public boolean targeted(User user) {
        return this.recipient.isSameAs(user);
    }

    public UUID getId() {
        return id;
    }

    public User getRecipient() {
        return recipient;
    }

    public NotificationType getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
