package br.app.vizo.core.problem;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class StatusUpdate {

    private final UUID id;
    private final Problem problem;
    private final String text;
    private final ProblemStatus status;
    private final Instant emittedAt;

    public StatusUpdate(Problem problem, String text, ProblemStatus status) {
        this.id = UUID.randomUUID();
        this.problem = problem;
        this.text = text;
        this.status = status;
        this.emittedAt = Instant.now();
    }

    public boolean refersTo(Problem problem) {
        return this.problem.isSameAS(problem);
    }
}
