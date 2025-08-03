package br.app.vizo.application.exception.base;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
