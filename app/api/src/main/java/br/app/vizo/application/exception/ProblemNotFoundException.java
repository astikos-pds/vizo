package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class ProblemNotFoundException extends NotFoundException {
    public ProblemNotFoundException() {
        super("Problem not found.");
    }
}
