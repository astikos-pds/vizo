package br.app.vizo.core.assignment.exception;

public class InvalidAssignedUserException extends RuntimeException {
    public InvalidAssignedUserException() {
        super("Assigned user must be associated with a valid affiliated user and municipality, and must have either a custom permission or a permission preset.");
    }
}
