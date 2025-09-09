package br.app.vizo.core.user.exception;

import br.app.vizo.core.DomainException;

public class InvalidHashedPasswordException extends DomainException {
    public InvalidHashedPasswordException() {
        super("Hashed password must not be null or blank.");
    }
}
