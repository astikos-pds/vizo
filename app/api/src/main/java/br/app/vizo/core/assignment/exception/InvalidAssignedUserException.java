package br.app.vizo.core.assignment.exception;

import br.app.vizo.core.DomainException;

public class InvalidAssignedUserException extends DomainException {
    public InvalidAssignedUserException() {
        super("Assigned user must be associated with a valid affiliated user and municipality, and must have either a custom permission or a permission preset.");
    }
}
