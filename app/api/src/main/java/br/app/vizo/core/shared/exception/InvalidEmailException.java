package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException() {
        super("Invalid email format.");
    }
}
