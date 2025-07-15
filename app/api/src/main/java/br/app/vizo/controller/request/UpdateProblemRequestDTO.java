package br.app.vizo.controller.request;

import br.app.vizo.domain.problem.ProblemStatus;

public record UpdateProblemRequestDTO(
        ProblemStatus status
) {
}
