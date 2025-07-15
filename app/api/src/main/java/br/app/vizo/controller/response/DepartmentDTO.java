package br.app.vizo.controller.response;

import java.time.Instant;
import java.util.UUID;

public record DepartmentDTO(
        UUID id,
        MunicipalityDTO municipality,
        String name,
        String iconUrl,
        String colorHex,
        OfficialDTO createdBy,
        Instant createdAt,
        Instant updatedAt
) {
}
