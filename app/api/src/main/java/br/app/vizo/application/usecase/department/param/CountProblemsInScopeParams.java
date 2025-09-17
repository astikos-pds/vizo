package br.app.vizo.application.usecase.department.param;

import br.app.vizo.core.problem.ProblemStatus;

public record CountProblemsInScopeParams(
        ProblemStatus status
) {
}
