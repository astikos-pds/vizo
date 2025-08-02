package br.app.vizo.core.shared.coordinates;

import br.app.vizo.core.shared.exception.InvalidLongitudeException;

public record Longitude(Double value) {

    public Longitude {
        if (value == null || value.isInfinite() || value.isNaN() || value < -180 || value > 180) {
            throw new InvalidLongitudeException();
        }
    }
}
