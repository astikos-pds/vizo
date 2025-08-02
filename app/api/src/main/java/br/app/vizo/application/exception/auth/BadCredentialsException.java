package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.UnauthorizedException;

public class BadCredentialsException extends UnauthorizedException {
    public BadCredentialsException() {
        super("Bad credentials.");
    }
}
