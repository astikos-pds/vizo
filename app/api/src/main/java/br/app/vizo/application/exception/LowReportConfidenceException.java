package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.UnprocessableEntityException;

public class LowReportConfidenceException extends UnprocessableEntityException {
    public LowReportConfidenceException() {
        super("Report confidence is too low");
    }
}
