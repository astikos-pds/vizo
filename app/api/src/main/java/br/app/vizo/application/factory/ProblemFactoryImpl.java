package br.app.vizo.application.factory;

import br.app.vizo.application.Factory;
import br.app.vizo.core.problem.*;
import br.app.vizo.core.shared.Coordinates;
import br.app.vizo.core.shared.Credibility;
import br.app.vizo.core.shared.MutationTimestamps;

import java.util.UUID;

@Factory
public class ProblemFactoryImpl implements ProblemFactory {

    @Override
    public Problem create(Coordinates coordinates, ProblemType problemType) {
        return new Problem(
                UUID.randomUUID(),
                coordinates,
                problemType,
                ProblemStatus.ANALYSIS,
                Credibility.of(0.0),
                false,
                new MutationTimestamps(),
                new ReportingTimeline(),
                null
        );
    }
}
