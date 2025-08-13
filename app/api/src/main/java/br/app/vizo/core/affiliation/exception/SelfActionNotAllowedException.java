package br.app.vizo.core.affiliation.exception;

import br.app.vizo.core.IllegalException;

public class SelfActionNotAllowedException extends IllegalException {
    public SelfActionNotAllowedException() {
        super("You cannot execute this action on yourself.");
    }
}
