package br.app.vizo.core.report.exception;

import br.app.vizo.core.DomainException;

public class TooManyImagesException extends DomainException {
    public TooManyImagesException() {
        super("Reports can have at most 5 images.");
    }
}
