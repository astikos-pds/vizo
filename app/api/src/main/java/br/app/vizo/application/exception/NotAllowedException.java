package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class NotAllowedException extends ForbiddenException {
    public NotAllowedException() {
        super("You are not allowed to see or modify this resource.");
    }
}
