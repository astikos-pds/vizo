package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class MustBeAdminException extends ForbiddenException {
    public MustBeAdminException() {
        super("You need to be an admin to execute this action.");
    }
}
