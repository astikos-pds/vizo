package br.app.vizo.application.usecase.department.request;

import br.app.vizo.core.problem.ProblemStatus;

public record ChangeProblemStatusRequestDTO(ProblemStatus status) {
}
