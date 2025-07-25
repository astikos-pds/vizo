package br.app.vizo.core.shared.exception;

public class InvalidOrdinateException extends RuntimeException {
    public InvalidOrdinateException() {
        super("Latitude and longitude must not be null, infinite or zero.");
    }
}
