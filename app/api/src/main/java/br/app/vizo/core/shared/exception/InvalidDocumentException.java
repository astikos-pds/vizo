package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidDocumentException extends DomainException {
    public InvalidDocumentException() {
        super("Invalid document.");
    }
}
