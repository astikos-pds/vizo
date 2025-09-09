package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidTimestampException extends DomainException {
    public InvalidTimestampException() {
        super("Invalid timestamp.");
    }
}
