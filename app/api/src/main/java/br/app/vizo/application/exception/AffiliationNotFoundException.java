package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class AffiliationNotFoundException extends NotFoundException {
    public AffiliationNotFoundException() {
        super("Affiliation not found.");
    }
}
