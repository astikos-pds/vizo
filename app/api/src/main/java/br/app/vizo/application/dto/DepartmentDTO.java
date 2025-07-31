package br.app.vizo.application.dto;

import br.app.vizo.core.problem.ProblemType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record DepartmentDTO(
        UUID id,
        MunicipalityDTO municipality,
        AffiliatedUserDTO creator,
        String name,
        String colorHex,
        String iconUrl,
        Set<ProblemType> scope,
        Instant createdAt,
        Instant updatedAt
) {
}
