package br.app.vizo.core.shared.exception;

import br.app.vizo.core.DomainException;

public class InvalidCredibilityException extends DomainException {
    public InvalidCredibilityException() {
        super("Credibility points must be greater than zero.");
    }
}
