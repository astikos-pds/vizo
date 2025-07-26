package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidNameException;

public record Name(
        String value
) {

    public Name {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException();
        }
    }
}
