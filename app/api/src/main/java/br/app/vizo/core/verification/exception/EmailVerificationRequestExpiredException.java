package br.app.vizo.core.verification.exception;

import br.app.vizo.core.DomainException;

public class EmailVerificationRequestExpiredException extends DomainException {
    public EmailVerificationRequestExpiredException() {
        super("Verification request expired. Try again.");
    }
}
