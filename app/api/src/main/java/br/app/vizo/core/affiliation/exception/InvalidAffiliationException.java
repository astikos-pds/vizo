package br.app.vizo.core.affiliation.exception;

import br.app.vizo.core.DomainException;

public class InvalidAffiliationException extends DomainException {
    public InvalidAffiliationException() {
        super("The given institutional e-mail is not compatible with municipality's domain.");
    }
}
