package br.app.vizo.core.interest.exception;

import br.app.vizo.core.DomainException;

public class InvalidRadiusException extends DomainException {
    public InvalidRadiusException(String message) {
        super(message);
    }
}
