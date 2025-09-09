package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.UnauthorizedException;

public class RefreshTokenNotRecognizedException extends UnauthorizedException {
    public RefreshTokenNotRecognizedException() {
        super("Refresh token not recognized, maybe used or expired.");
    }
}
