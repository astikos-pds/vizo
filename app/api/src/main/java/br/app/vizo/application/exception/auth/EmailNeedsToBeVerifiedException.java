package br.app.vizo.application.exception.auth;

public class EmailNeedsToBeVerifiedException extends RuntimeException {
    public EmailNeedsToBeVerifiedException() {
        super("E-mail needs to be verified in order to execute this action.");
    }
}
