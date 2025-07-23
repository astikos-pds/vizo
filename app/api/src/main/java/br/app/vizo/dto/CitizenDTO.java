package br.app.vizo.dto;

import java.time.Instant;
import java.util.UUID;

public record CitizenDTO(
        UUID id,
        String document,
        String email,
        String name,
        AvatarDTO avatar,
        Long credibilityPoints,
        Instant createdAt,
        Instant updatedAt
) {
}
