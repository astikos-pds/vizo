package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidNameException;
import jakarta.persistence.Column;

public record Name(
        @Column(name = "name", nullable = false) String value
) {

    public Name {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException();
        }
    }
}
