package br.app.vizo.core.affiliation.exception;

public class SelfActionNotAllowedException extends RuntimeException {
    public SelfActionNotAllowedException() {
        super("You cannot execute this action on yourself.");
    }
}
