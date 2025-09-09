package br.app.vizo.application.exception.auth;

import br.app.vizo.application.exception.base.NotFoundException;

public class EmailVerificationRequestNotFound extends NotFoundException {
    public EmailVerificationRequestNotFound() {
        super("Email verification request not found.");
    }
}
