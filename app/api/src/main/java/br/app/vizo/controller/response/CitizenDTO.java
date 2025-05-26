package br.app.vizo.controller.response;

import java.time.Instant;
import java.util.UUID;

public record CitizenDTO(
        UUID id,
        String document,
        String email,
        String name,
        Long credibilityPoints,
        Instant createdAt,
        Instant updatedAt
) {
}
