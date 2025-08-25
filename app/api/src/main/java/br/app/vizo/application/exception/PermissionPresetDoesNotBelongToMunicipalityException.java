package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.ForbiddenException;

public class PermissionPresetDoesNotBelongToMunicipalityException extends ForbiddenException {
    public PermissionPresetDoesNotBelongToMunicipalityException() {
        super("Permission preset does not belongs to this municipality.");
    }
}
