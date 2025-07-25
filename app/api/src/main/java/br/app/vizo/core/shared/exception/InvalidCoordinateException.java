package br.app.vizo.core.shared.exception;

public class InvalidCoordinateException extends RuntimeException {
    public InvalidCoordinateException() {
        super("Coordinate must be a pair of non null, float point number values.");
    }
}
