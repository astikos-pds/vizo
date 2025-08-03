package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class AssignmentNotFoundException extends NotFoundException {
    public AssignmentNotFoundException() {
        super("Assignment not found.");
    }
}
