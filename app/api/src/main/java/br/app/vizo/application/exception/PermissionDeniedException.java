package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
