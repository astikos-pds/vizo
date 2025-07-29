package br.app.vizo.core.problem;

import br.app.vizo.core.shared.exception.InvalidTimestampException;

import java.time.Instant;

public class ReportingTimeline {

    private final Instant firstReportedAt;
    private Instant lastReportedAt;

    public ReportingTimeline(Instant firstReportedAt, Instant lastReportedAt) {
        if (firstReportedAt == null || firstReportedAt.isBefore(Instant.now())) {
            throw new InvalidTimestampException();
        }
        this.firstReportedAt = firstReportedAt;

        if (lastReportedAt == null || lastReportedAt.isBefore(Instant.now())) {
            throw new InvalidTimestampException();
        }
        this.lastReportedAt = lastReportedAt;
    }

    public ReportingTimeline() {
        this(Instant.now(), Instant.now());
    }

    public void update() {
        lastReportedAt = Instant.now();
    }

    public Instant getFirstReportedAt() {
        return firstReportedAt;
    }

    public Instant getLastReportedAt() {
        return lastReportedAt;
    }
}
