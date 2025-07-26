package br.app.vizo.core.problem;

import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;
import br.app.vizo.domain.problem.ProblemStatus;
import br.app.vizo.domain.problem.ProblemType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Problem {

    @Getter private final UUID id;
    private final Coordinates coordinates;
    @Getter private final ProblemType type;
    @Getter private ProblemStatus status;
    private Credibility accumulatedCredibility;

    private final MutationTimestamps timestamps;

    public static Problem report(Coordinates coordinates, ProblemType problemType) {
        return new Problem(
                UUID.randomUUID(),
                coordinates,
                problemType,
                ProblemStatus.ANALYSIS,
                Credibility.of(0.0),
                MutationTimestamps.create()
        );
    }

    public void updateStatusTo(ProblemStatus status) {
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

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

}
