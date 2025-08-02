package br.app.vizo.core.shared.exception;

public class InvalidLongitudeException extends RuntimeException {
    public InvalidLongitudeException() {
        super("Longitude must be a non null, finite number between -180 and 180.");
    }
}
