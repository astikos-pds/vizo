package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ConflictException;

public class PresetNameAlreadyInUseException extends ConflictException {
    public PresetNameAlreadyInUseException() {
        super("Permission preset name already in use in this municipality.");
    }
}
