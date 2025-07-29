package br.app.vizo.application.dto;

import br.app.vizo.core.problem.ProblemStatus;
import br.app.vizo.core.problem.ProblemType;

import java.time.Instant;
import java.util.UUID;

public record ProblemDTO(
        UUID id,
        Double latitude,
        Double longitude,
        ProblemType type,
        ProblemStatus status,
        Double accumulatedCredibility,
        boolean validated,
        Instant createdAt,
        Instant updatedAt,
        Instant firstReportedAt,
        Instant lastReportedAt,
        Instant resolvedAt
) {}
