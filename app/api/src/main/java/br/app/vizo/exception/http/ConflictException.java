package br.app.vizo.exception.http;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
