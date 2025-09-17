package br.app.vizo.application.usecase.department.param;

import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.problem.ProblemType;

import java.time.LocalDate;
import java.util.Set;

public record GetProblemsInScopeStatisticsParams(
        LocalDate start,
        LocalDate end,
        Set<ProblemStatus> statuses,
        Set<ProblemType> types
) {
}
