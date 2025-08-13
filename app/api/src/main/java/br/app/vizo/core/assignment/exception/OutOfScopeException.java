package br.app.vizo.core.assignment.exception;

import br.app.vizo.core.DomainException;

public class OutOfScopeException extends DomainException {
    public OutOfScopeException() {
        super("This problem is out of department's scope.");
    }
}
