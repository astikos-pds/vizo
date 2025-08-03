package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class AssignmentRequiredException extends ForbiddenException {
    public AssignmentRequiredException() {
        super("You need to be assigned to execute this action.");
    }
}
