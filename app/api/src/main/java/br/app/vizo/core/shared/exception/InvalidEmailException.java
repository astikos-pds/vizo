package br.app.vizo.core.shared.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Invalid email format.");
    }
}
