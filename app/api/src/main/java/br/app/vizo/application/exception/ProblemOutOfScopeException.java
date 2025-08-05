package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class ProblemOutOfScopeException extends ForbiddenException {
    public ProblemOutOfScopeException() {
        super("This problem is out of department's scope.");
    }
}
