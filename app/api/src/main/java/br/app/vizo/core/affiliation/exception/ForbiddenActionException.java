package br.app.vizo.core.affiliation.exception;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException() {
        super("You need to be an admin to execute this action.");
    }
}
