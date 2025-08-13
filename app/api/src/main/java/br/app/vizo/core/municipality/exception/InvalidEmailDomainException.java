package br.app.vizo.core.municipality.exception;

import br.app.vizo.core.DomainException;

public class InvalidEmailDomainException extends DomainException {
    public InvalidEmailDomainException() {
        super("Invalid e-mail domain.");
    }
}
