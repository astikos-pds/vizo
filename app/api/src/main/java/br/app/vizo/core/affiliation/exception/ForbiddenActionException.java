package br.app.vizo.core.affiliation.exception;

import br.app.vizo.core.IllegalException;

public class ForbiddenActionException extends IllegalException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
