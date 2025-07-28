package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidTimestampException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public class MutationTimestamps {

    @Column(name = "created_at", nullable = false)
    private final Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private MutationTimestamps() {
        this(Instant.now(), Instant.now());
    }

    public MutationTimestamps(Instant createdAt, Instant updatedAt) {
        if (createdAt == null || createdAt.isBefore(Instant.now())) {
            throw new InvalidTimestampException();
        }
        this.createdAt = createdAt;

        if (updatedAt == null || updatedAt.isBefore(Instant.now()) || updatedAt.isBefore(createdAt)) {
            throw new InvalidTimestampException();
        }
        this.updatedAt = updatedAt;
    }

    public static MutationTimestamps create() {
        return new MutationTimestamps();
    }

    public void update() {
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
