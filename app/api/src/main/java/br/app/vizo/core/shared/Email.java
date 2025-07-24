package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidEmailException;
import lombok.Getter;

@Getter
public class Email {

    private String value;

    public Email(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        if (value == null || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException();
        }

        this.value = value;
    }
}
