package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.UnprocessableEntityException;

public class ReportDiscrepancyTooBigException extends UnprocessableEntityException {
    public ReportDiscrepancyTooBigException() {
        super("When editing a report, you cannot make discrepancies so large that you change the problem type.");
    }
}
