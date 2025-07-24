package br.app.vizo.core.report;

import br.app.vizo.core.report.exception.InvalidDescriptionException;

public record Description(String value) {

    public Description {
        if (value == null || value.isBlank()) {
            throw new InvalidDescriptionException("Description must not be null or blank.");
        }
        if (value.length() > 255) {
            throw new InvalidDescriptionException("Description length must be smaller than 255 characters.");
        }
    }
}
