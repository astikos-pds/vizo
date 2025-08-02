package br.app.vizo.core.shared.exception;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException() {
        super("Image URL must not be null or blank.");
    }
}
