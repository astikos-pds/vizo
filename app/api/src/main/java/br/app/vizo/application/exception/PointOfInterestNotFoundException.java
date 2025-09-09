package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class PointOfInterestNotFoundException extends NotFoundException {
    public PointOfInterestNotFoundException() {
        super("Point of interest not found.");
    }
}
