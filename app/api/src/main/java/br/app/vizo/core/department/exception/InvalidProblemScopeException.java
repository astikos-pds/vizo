package br.app.vizo.core.department.exception;

public class InvalidProblemScopeException extends RuntimeException {
    public InvalidProblemScopeException() {
        super("Problem scope requires at least one element.");
    }
}
