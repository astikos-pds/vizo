package br.app.vizo.core.shared.exception;

public class InvalidImage extends RuntimeException {
    public InvalidImage() {
        super("Image URL must not be null or blank.");
    }
}
