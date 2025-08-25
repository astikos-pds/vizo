package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ConflictException;

public class InstitutionalEmailAlreadyInUse extends ConflictException {
    public InstitutionalEmailAlreadyInUse() {
        super("Institutional e-mail already in use.");
    }
}
