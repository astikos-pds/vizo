package br.app.vizo.core.verification.exception;

public class EmailVerificationRequestExpiredException extends RuntimeException {
    public EmailVerificationRequestExpiredException() {
        super("Verification request expired. Try again.");
    }
}
