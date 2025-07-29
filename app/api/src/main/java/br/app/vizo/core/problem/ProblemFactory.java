package br.app.vizo.core.problem;

import br.app.vizo.core.shared.Coordinates;

public interface ProblemFactory {

    Problem create(Coordinates coordinates, ProblemType problemType);
}
