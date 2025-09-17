package br.app.vizo.core.problem;

import java.time.LocalDate;

public record ProblemStatistics(
        LocalDate date,
        Long count
) {
}
