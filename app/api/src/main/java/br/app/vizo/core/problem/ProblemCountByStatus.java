package br.app.vizo.core.problem;

public record ProblemCountByStatus(
        ProblemStatus status,
        Long count
) {
}
