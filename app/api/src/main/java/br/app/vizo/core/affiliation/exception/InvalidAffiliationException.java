package br.app.vizo.core.affiliation.exception;

public class InvalidAffiliationException extends RuntimeException {
    public InvalidAffiliationException() {
        super("The given institutional e-mail is not compatible with municipality's domain.");
    }
}
