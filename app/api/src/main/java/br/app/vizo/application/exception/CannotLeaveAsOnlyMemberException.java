package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ConflictException;

public class CannotLeaveAsOnlyMemberException extends ConflictException {
    public CannotLeaveAsOnlyMemberException() {
        super("You cannot leave municipality as their only member.");
    }
}
