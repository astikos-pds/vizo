package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidTimestampException;
import lombok.Getter;

import java.time.Instant;

@Getter
public class MutationTimestamps {

    private final Instant createdAt;
    private Instant updatedAt;

    private MutationTimestamps() {
        this(Instant.now(), Instant.now());
    }

    private MutationTimestamps(Instant createdAt, Instant updatedAt) {
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
}
