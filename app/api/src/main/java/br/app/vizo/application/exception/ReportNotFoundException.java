package br.app.vizo.application.exception;

import br.app.vizo.application.exception.base.NotFoundException;

public class ReportNotFoundException extends NotFoundException {
    public ReportNotFoundException() {
        super("Report not found.");
    }
}
