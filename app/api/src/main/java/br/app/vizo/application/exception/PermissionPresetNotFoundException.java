package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class PermissionPresetNotFoundException extends NotFoundException {
    public PermissionPresetNotFoundException() {
        super("Permission preset not found.");
    }
}
