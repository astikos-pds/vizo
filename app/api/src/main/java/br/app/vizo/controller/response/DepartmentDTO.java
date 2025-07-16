package br.app.vizo.controller.response;

import br.app.vizo.domain.problem.ProblemType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record DepartmentDTO(
        UUID id,
        MunicipalityDTO municipality,
        String name,
        String iconUrl,
        String colorHex,
        Set<ProblemType> problemTypes,
        OfficialDTO createdBy,
        Instant createdAt,
        Instant updatedAt
) {
}
