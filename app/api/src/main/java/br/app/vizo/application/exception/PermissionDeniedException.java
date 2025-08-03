package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    public PermissionDeniedException() {
        super("You do not have enough permission to execute this action.");
    }
}
