package br.app.vizo.core.notification;

import br.app.vizo.core.problem.ProblemType;

import java.time.Instant;
import java.util.UUID;

public record NewProblemEvent (
        UUID problemId,
        ProblemType problemType,
        Double problemLatitude,
        Double problemLongitude,
        UUID firstReportId,
        String firstReportDescription,
        Instant occurredAt
) implements DomainEvent {
}
