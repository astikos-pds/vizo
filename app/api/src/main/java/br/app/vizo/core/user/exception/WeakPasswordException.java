package br.app.vizo.core.user.exception;

import br.app.vizo.core.DomainException;

public class WeakPasswordException extends DomainException {
    public WeakPasswordException(String message) {
        super(message);
    }
}
