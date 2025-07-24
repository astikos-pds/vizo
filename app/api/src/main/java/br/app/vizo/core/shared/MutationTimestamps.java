package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidTimestampException;

import java.time.Instant;

public class MutationTimestamps {

    private final Instant createdAt;
    private Instant updatedAt;

    public MutationTimestamps() {
        this(Instant.now(), Instant.now());
    }

    public MutationTimestamps(Instant createdAt, Instant updatedAt) {
        if (createdAt == null || createdAt.isBefore(Instant.now())) {
            throw new InvalidTimestampException();
        }
        this.createdAt = createdAt;

        if (updatedAt == null || updatedAt.isBefore(Instant.now())) {
            throw new InvalidTimestampException();
        }
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static MutationTimestamps create() {
        return new MutationTimestamps();
    }

    public void update() {
        this.updatedAt = Instant.now();
    }
}
