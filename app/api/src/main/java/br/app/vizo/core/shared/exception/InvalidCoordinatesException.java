package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidCoordinatesException extends DomainException {
    public InvalidCoordinatesException() {
        super("Coordinate must be a pair of non null, float point number values.");
    }
}
