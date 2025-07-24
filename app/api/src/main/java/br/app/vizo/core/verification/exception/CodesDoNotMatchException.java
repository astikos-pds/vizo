package br.app.vizo.core.verification.exception;

public class CodesDoNotMatchException extends RuntimeException {
    public CodesDoNotMatchException() {
        super("Codes don't match. Try again.");
    }
}
