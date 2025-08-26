package br.app.vizo.application.dto;

import java.time.Instant;
import java.util.UUID;

public record PointOfInterestDTO(
        UUID id,
        UserDTO user,
        String name,
        Double latitude,
        Double longitude,
        Double radius,
        Instant createdAt,
        Instant updatedAt
) {
}
