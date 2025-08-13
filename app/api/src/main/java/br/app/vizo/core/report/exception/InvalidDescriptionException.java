package br.app.vizo.core.report.exception;

import br.app.vizo.core.DomainException;

public class InvalidDescriptionException extends DomainException {
    public InvalidDescriptionException(String message) {
        super(message);
    }
}
