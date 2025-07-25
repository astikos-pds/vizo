package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidOrdinateException;

public record Ordinate(
        Double value
) {
    public Ordinate {
        if (value == null || value.isInfinite() || value.isNaN() || value == 0) {
            throw new InvalidOrdinateException();
        }
    }
}
