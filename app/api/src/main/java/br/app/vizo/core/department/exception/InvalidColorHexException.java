package br.app.vizo.core.department.exception;

public class InvalidColorHexException extends RuntimeException {
    public InvalidColorHexException() {
        super("Invalid color hex.");
    }
}
