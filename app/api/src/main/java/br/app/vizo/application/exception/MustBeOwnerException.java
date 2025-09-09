package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class MustBeOwnerException extends ForbiddenException {
    public MustBeOwnerException() {
        super("You are not allowed to see or modify this point of interest.");
    }
}
