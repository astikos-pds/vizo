package br.app.vizo.core.shared.exception;

public class InvalidCredibilityException extends RuntimeException {
    public InvalidCredibilityException() {
        super("Credibility points must be greater than zero.");
    }
}
