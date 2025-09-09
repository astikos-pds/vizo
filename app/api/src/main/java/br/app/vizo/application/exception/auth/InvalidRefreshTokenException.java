package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.UnauthorizedException;

public class InvalidRefreshTokenException extends UnauthorizedException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token.");
    }
}
