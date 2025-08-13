package br.app.vizo.core.interest.exception;

import br.app.vizo.core.DomainException;

public class MustHaveAnOwnerException extends DomainException {
    public MustHaveAnOwnerException() {
        super("Point of interest must have an owner.");
    }
}
