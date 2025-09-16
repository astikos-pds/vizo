package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.UnprocessableEntityException;

public class CannotModifyReportException extends UnprocessableEntityException {
    public CannotModifyReportException() {
        super("You can only update or delete a report if it is under analysis by the municipal authorities.");
    }
}
