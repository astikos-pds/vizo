package br.app.vizo.core.municipality.exception;

public class InvalidEmailDomainException extends RuntimeException {
    public InvalidEmailDomainException() {
        super("Invalid e-mail domain.");
    }
}
