package br.app.vizo.core.notification.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}