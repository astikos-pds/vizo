package br.app.vizo.core.shared.exception;

public class InvalidDocumentException extends RuntimeException {
    public InvalidDocumentException() {
        super("Invalid document.");
    }
}
