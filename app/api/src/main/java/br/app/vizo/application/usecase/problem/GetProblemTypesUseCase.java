package br.app.vizo.application.usecase.problem;

import br.app.vizo.application.UseCase;
import br.app.vizo.core.problem.ProblemType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UseCase
public class GetProblemTypesUseCase {

    public Set<ProblemType> execute() {
        return Arrays.stream(ProblemType.values())
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
