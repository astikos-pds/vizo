package br.app.vizo.core.poi.exception;

import br.app.vizo.core.DomainException;

public class InvalidPointOfInterestException extends DomainException {
    public InvalidPointOfInterestException() {
        super("Point of interest must have an id, an owner, coordinates, a radius and timestamps.");
    }
}
