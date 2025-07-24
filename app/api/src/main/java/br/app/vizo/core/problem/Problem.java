package br.app.vizo.core.problem;

import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.domain.problem.ProblemStatus;
import br.app.vizo.domain.problem.ProblemType;
import lombok.Getter;

import java.util.UUID;

public class Problem {

    @Getter
    private final UUID id;

    @Getter
    private final Coordinates coordinates;

    @Getter
    private final ProblemType type;

    @Getter
    private ProblemStatus status;

    private Credibility accumulatedCredibility;

    private final MutationTimestamps timestamps;

    private Problem(
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
        this.accumulatedCredibility = this.accumulatedCredibility.increase(points);
        this.timestamps.update();
    }

}
