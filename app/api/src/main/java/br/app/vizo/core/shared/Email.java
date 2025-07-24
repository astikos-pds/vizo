package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidEmailException;

public class Email {

    private String value;

    public Email(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException();
        }

        this.value = value;
    }
}
