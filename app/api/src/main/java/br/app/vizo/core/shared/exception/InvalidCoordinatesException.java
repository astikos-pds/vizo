package br.app.vizo.core.shared.exception;

public class InvalidCoordinatesException extends RuntimeException {
    public InvalidCoordinatesException() {
        super("Coordinate must be a pair of non null, float point number values.");
    }
}
