package br.app.vizo.core.problem.exception;

import br.app.vizo.core.DomainException;

public class InvalidProblemException extends DomainException {
    public InvalidProblemException() {
        super("All problem's attributes must not be null.");
    }
}
