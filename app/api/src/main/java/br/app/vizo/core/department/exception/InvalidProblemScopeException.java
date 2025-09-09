package br.app.vizo.core.department.exception;

import br.app.vizo.core.DomainException;

public class InvalidProblemScopeException extends DomainException {
    public InvalidProblemScopeException() {
        super("Problem scope requires at least one element.");
    }
}
