package br.app.vizo.core.interest;

import br.app.vizo.core.interest.exception.InvalidRadiusException;

public record Radius(
        Double value
) {
    private static final int MAX_VALUE_IN_METERS = 2000;

    public Radius {
        if (value == null || value.isNaN() || value.isInfinite() || value <= 0) {
            throw new InvalidRadiusException("Radius must be a non null, float point, positive number.");
        }

        if (value > MAX_VALUE_IN_METERS) {
            throw new InvalidRadiusException("Radius must be smaller than %s meters".formatted(MAX_VALUE_IN_METERS));
        }
    }

    public static Radius of(Double value) {
        return new Radius(value);
    }
}
