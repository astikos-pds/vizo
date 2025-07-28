package br.app.vizo.application.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String document,
        String email,
        String name,
        String avatarUrl,
        Double credibilityPoints,
        Instant createdAt,
        Instant updatedAt
) {
}

