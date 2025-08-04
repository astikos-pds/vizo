package br.app.vizo.core.user.exception;

public class ChangePasswordRequestExpiredException extends RuntimeException {
    public ChangePasswordRequestExpiredException() {
        super("Change password request expired.");
    }
}
