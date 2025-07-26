package br.app.vizo.core.assignment.exception;

public class OutOfScopeException extends RuntimeException {
    public OutOfScopeException() {
        super("This problem is out of department's scope.");
    }
}
