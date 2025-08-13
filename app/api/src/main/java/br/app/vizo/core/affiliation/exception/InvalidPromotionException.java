package br.app.vizo.core.affiliation.exception;

import br.app.vizo.core.DomainException;

public class InvalidPromotionException extends DomainException {
    public InvalidPromotionException() {
        super("Affiliated user needs to be approved in order to promote to admin.");
    }
}
