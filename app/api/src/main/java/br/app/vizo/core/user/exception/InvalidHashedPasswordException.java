package br.app.vizo.core.user.exception;

public class InvalidHashedPasswordException extends RuntimeException {
    public InvalidHashedPasswordException() {
        super("Hashed password must not be null or blank.");
    }
}
