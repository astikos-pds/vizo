package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidImageException extends DomainException {
    public InvalidImageException() {
        super("Image URL must not be null or blank.");
    }
}
