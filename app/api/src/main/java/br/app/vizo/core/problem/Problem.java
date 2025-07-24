package br.app.vizo.core.problem;

import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.domain.problem.ProblemStatus;
import br.app.vizo.domain.problem.ProblemType;
import java.util.UUID;

public class Problem {

    private final UUID id;
    private final Coordinates coordinates;
    private final ProblemType type;
    private ProblemStatus status;
    private final Credibility accumulatedCredibility;
    private final MutationTimestamps timestamps;

    public Problem(
            UUID id,
            Coordinates coordinates,
            ProblemType type,
            ProblemStatus status,
            Credibility accumulatedCredibility,
            MutationTimestamps timestamps
    ) {
        this.id = id;
        this.coordinates = coordinates;
        this.type = type;
        this.status = status;
        this.accumulatedCredibility = accumulatedCredibility;
        this.timestamps = timestamps;
    }

    public static Problem create(Coordinates coordinates, ProblemType problemType) {
        return new Problem(
                UUID.randomUUID(),
                coordinates,
                problemType,
                ProblemStatus.ANALYSIS,
                Credibility.of(0.0),
                MutationTimestamps.create()
        );
    }

    public void changeStatusTo(ProblemStatus status) {
        setStatus(status);
    }

    private void setStatus(ProblemStatus status) {
        this.status = status;
        this.timestamps.update();
    }

    public void increaseCredibility(Double points) {
        this.accumulatedCredibility.increase(points);
        this.timestamps.update();
    }

    public void increaseCredibility(Credibility credibility) {
        this.accumulatedCredibility.increase(credibility.getPoints());
        this.timestamps.update();
    }

    public UUID getId() {
        return id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ProblemType getType() {
        return type;
    }

    public ProblemStatus getStatus() {
        return status;
    }
}
