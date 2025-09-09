package br.app.vizo.core.department.exception;

import br.app.vizo.core.DomainException;

public class InvalidDepartmentException extends DomainException {
    public InvalidDepartmentException() {
        super("Department must be related to an existing municipality and be created by a valid affiliated user, having a name, an color and a scope.");
    }
}
