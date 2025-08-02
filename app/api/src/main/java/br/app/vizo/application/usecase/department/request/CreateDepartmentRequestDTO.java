package br.app.vizo.application.usecase.department.request;

import br.app.vizo.core.problem.ProblemType;

import java.util.Set;

public record CreateDepartmentRequestDTO(
        String name,
        String iconUrl,
        String colorHex,
        Set<ProblemType> problemTypes
) {
}
