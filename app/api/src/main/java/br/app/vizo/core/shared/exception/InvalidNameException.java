package br.app.vizo.core.shared.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Name is required.");
    }
}
