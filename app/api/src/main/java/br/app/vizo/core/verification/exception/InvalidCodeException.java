package br.app.vizo.core.verification.exception;

import br.app.vizo.core.DomainException;

public class InvalidCodeException extends DomainException {
    public InvalidCodeException(String message) {
        super(message);
    }
}
