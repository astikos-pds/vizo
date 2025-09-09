package br.app.vizo.core.shared.coordinates;

import br.app.vizo.core.shared.exception.InvalidLatitudeException;

public record Latitude(Double value) {

    public Latitude {
        if (value == null || value.isInfinite() || value.isNaN() || value < -90 || value > 90) {
            throw new InvalidLatitudeException();
        }
    }
}
