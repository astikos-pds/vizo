package br.app.vizo.core.affiliation.exception;

public class InvalidPromotionException extends RuntimeException {
    public InvalidPromotionException() {
        super("Affiliated user needs to be approved in order to promote to admin.");
    }
}
