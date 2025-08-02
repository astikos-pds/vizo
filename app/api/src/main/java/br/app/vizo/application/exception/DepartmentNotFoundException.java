package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException() {
        super("Department not found.");
    }
}
