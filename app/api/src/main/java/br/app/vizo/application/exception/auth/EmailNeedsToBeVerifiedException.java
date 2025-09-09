package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.UnauthorizedException;

public class EmailNeedsToBeVerifiedException extends UnauthorizedException {
    public EmailNeedsToBeVerifiedException() {
        super("E-mail needs to be verified in order to execute this action.");
    }
}
