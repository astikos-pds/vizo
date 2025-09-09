package br.app.vizo.core.verification.exception;

import br.app.vizo.core.DomainException;

public class CodesDoNotMatchException extends DomainException {
    public CodesDoNotMatchException() {
        super("Codes don't match. Try again.");
    }
}
