package br.app.vizo.core.poi.exception;

import br.app.vizo.core.DomainException;

public class InvalidRadiusException extends DomainException {
    public InvalidRadiusException(String message) {
        super(message);
    }
}
