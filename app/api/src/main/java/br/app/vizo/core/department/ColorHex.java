package br.app.vizo.core.department;

import br.app.vizo.core.department.exception.InvalidColorHexException;

public record ColorHex(
        String value
) {

    public ColorHex {
        if (value == null || value.isBlank() || !value.matches("/^#?([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/")) {
            throw new InvalidColorHexException();
        }
    }
}
