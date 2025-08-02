package br.app.vizo.core.shared.exception;

public class InvalidLatitudeException extends RuntimeException {
    public InvalidLatitudeException() {
        super("Latitude must be a non null, finite number between -90 and 90.");
    }
}
