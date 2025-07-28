package br.app.vizo.core.user;

import java.util.UUID;

public record UserId(
        UUID value
) {

    public UserId() {
        this(UUID.randomUUID());
    }
}
