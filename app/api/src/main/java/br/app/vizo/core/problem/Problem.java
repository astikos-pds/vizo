package br.app.vizo.core.problem;

import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;

import java.time.Instant;
import java.util.UUID;

public class Problem {

    private final UUID id;
    private final Coordinates coordinates;
    private final ProblemType type;
    private ProblemStatus status;
    private Credibility accumulatedCredibility;
    private boolean validated;
    private final MutationTimestamps timestamps;
    private final ReportingTimeline reportingTimeline;
    private Instant resolvedAt;

    public Problem(UUID id, Coordinates coordinates, ProblemType type, ProblemStatus status, Credibility accumulatedCredibility, boolean validated, MutationTimestamps timestamps, ReportingTimeline reportingTimeline, Instant resolvedAt) {
        this.id = id;
        this.coordinates = coordinates;
        this.type = type;
        this.status = status;
        this.accumulatedCredibility = accumulatedCredibility;
        this.validated = validated;
        this.timestamps = timestamps;
        this.reportingTimeline = reportingTimeline;
        this.resolvedAt = resolvedAt;
    }

    public void updateStatusTo(ProblemStatus status) {
        this.status = status;
        if (status == ProblemStatus.RESOLVED) {
            resolvedAt = Instant.now();
        }
        this.timestamps.update();
    }

    public void increaseCredibility(Double reportCredibility) {
        this.accumulatedCredibility = this.accumulatedCredibility.accumulate(reportCredibility);

        if (accumulatedCredibility.points() > 120) {
            this.validated = true;
        }

        this.timestamps.update();
        this.reportingTimeline.update();
    }

    public UUID getId() {
        return id;
    }

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    public ProblemType getType() {
        return type;
    }

    public ProblemStatus getStatus() {
        return status;
    }

    public Double getAccumulatedCredibility() {
        return accumulatedCredibility.points();
    }

    public boolean isValidated() {
        return validated;
    }

    public Instant getCreatedAt() {
        return timestamps.getCreatedAt();
    }

    public Instant getUpdatedAt() {
        return timestamps.getUpdatedAt();
    }

    public Instant getFirstReportedAt() {
        return reportingTimeline.getFirstReportedAt();
    }

    public Instant getLastReportedAt() {
        return reportingTimeline.getLastReportedAt();
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public boolean isSameAS(Problem other) {
        return id.equals(other.getId());
    }
}
