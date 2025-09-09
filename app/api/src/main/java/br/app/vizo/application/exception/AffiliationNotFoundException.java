package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class AffiliationNotFoundException extends NotFoundException {

    public AffiliationNotFoundException(String message) {
        super(message);
    }

    public AffiliationNotFoundException() {
        this("Affiliation not found.");
    }
}
