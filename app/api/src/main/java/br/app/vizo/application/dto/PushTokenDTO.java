package br.app.vizo.application.dto;

import br.app.vizo.core.user.push.Platform;

import java.time.Instant;
import java.util.UUID;

public record PushTokenDTO(
        Long id,
        UUID userId,
        String token,
        Platform platform,
        Instant createdAt,
        Instant lastUsedAt
) {
}
