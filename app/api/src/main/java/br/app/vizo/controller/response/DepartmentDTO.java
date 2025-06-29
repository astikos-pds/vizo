package br.app.vizo.controller.response;

import java.time.Instant;
import java.util.UUID;

public record DepartmentDTO(
        UUID id,
        UUID municipalityId,
        String name,
        String iconUrl,
        String colorHex,
        UUID createdById,
        Instant createdAt,
        Instant updatedAt
) {
}
