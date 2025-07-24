package br.app.vizo.core.shared.exception;

public class InvalidTimestampException extends RuntimeException {
    public InvalidTimestampException() {
        super("Invalid timestamp.");
    }
}
