package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidEmailException;
import jakarta.persistence.Column;

public record Email(
        @Column(name = "email", nullable = false, unique = true) String value
) {

    public Email {
        if (value == null || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException();
        }
    }

}
