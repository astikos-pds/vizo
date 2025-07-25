package br.app.vizo.core.interest.exception;

public class MustHaveAnOwnerException extends RuntimeException {
    public MustHaveAnOwnerException() {
        super("Point of interest must have an owner.");
    }
}
