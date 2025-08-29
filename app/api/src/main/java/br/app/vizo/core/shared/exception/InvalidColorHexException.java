package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidColorHexException extends DomainException {
    public InvalidColorHexException() {
        super("Invalid color hex.");
    }
}
