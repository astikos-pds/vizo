package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class MunicipalityNotFoundException extends NotFoundException {
    public MunicipalityNotFoundException() {
        super("Municipality not found.");
    }
}
