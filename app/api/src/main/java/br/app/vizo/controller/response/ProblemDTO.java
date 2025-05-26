package br.app.vizo.controller.response;

import br.app.vizo.domain.problem.ProblemStatus;

import java.time.Instant;
import java.util.UUID;

public record ProblemDTO(
        UUID id,
        ProblemStatus status,
        Double latitude,
        Double longitude,
        Double accumulatedCredibility,
        Boolean validated,
        Instant firstReportedAt,
        Instant lastReportedAt
) {
}
