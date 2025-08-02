package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidEmailException;

public record Email(
        String value
) {

    public Email {
        if (value == null || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException();
        }
    }

    public String getDomain() {
        return value.split("@")[1];
    }
}
