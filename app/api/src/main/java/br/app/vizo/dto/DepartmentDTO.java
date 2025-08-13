package br.app.vizo.dto;

import java.time.Instant;
import java.util.UUID;

public record DepartmentDTO(
        UUID id,
        MunicipalityDTO municipality,
        String name,
        String iconUrl,
        String colorHex,
        UserDTO creator,
        Instant createdAt,
        Instant updatedAt
) {
}
