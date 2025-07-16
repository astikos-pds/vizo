package br.app.vizo.controller.request;

import br.app.vizo.domain.problem.ProblemType;

import java.util.Set;

public record CreateDepartmentRequestDTO(
        String name,
        String iconUrl,
        String colorHex,
        Set<ProblemType> problemTypes
) {
}
