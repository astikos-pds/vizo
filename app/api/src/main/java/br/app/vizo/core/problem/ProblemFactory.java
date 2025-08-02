package br.app.vizo.core.problem;

import br.app.vizo.core.shared.coordinates.Coordinates;

public interface ProblemFactory {

    Problem create(Coordinates coordinates, ProblemType problemType);
}
