package br.app.vizo.core.report.exception;

public class TooManyImagesException extends RuntimeException {
    public TooManyImagesException() {
        super("Reports can have at most 5 images.");
    }
}
