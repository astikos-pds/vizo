package br.app.vizo.core.notification;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}