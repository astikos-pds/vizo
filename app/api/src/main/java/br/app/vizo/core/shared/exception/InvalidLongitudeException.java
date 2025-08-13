package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidLongitudeException extends DomainException {
    public InvalidLongitudeException() {
        super("Longitude must be a non null, finite number between -180 and 180.");
    }
}
