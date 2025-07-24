package br.app.vizo.core.shared;

import java.time.Instant;

public class MutationTimestamps {

    private final Instant createdAt;
    private Instant updatedAt;

    public MutationTimestamps() {
        this(Instant.now(), Instant.now());
    }

    public MutationTimestamps(Instant createdAt, Instant updatedAt) {
        this.createdAt = createdAt;
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
