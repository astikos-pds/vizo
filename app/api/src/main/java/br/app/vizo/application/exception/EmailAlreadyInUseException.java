package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ConflictException;

public class EmailAlreadyInUseException extends ConflictException {
    public EmailAlreadyInUseException() {
        super("E-mail is already in use.");
    }
}
