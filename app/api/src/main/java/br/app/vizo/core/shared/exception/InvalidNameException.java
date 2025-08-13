package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidNameException extends DomainException {
    public InvalidNameException() {
        super("Name is required.");
    }
}
