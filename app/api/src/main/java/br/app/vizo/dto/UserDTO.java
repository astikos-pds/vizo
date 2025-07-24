package br.app.vizo.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String document,
        String email,
        String name,
        AvatarDTO avatar,
        Double credibilityPoints,
        Instant createdAt,
        Instant updatedAt
) {
}
