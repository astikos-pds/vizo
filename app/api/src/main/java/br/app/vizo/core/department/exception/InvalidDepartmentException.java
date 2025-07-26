package br.app.vizo.core.department.exception;

public class InvalidDepartmentException extends RuntimeException {
    public InvalidDepartmentException() {
        super("Department must be related to an existing municipality and be created by a valid affiliated user.");
    }
}
