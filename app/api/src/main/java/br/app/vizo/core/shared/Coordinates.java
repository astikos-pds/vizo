package br.app.vizo.core.shared;

import br.app.vizo.core.shared.exception.InvalidCoordinateException;

public record Coordinates(
        Ordinate latitude,
        Ordinate longitude
) {

    public Coordinates {
        if (latitude == null || longitude == null) {
            throw new InvalidCoordinateException();
        }
    }

    public static Coordinates of(Double latitude, Double longitude) {
        return new Coordinates(
                new Ordinate(latitude), new Ordinate(longitude)
        );
    }

    public Double getLatitude() {
        return latitude.value();
    }

    public Double getLongitude() {
        return longitude.value();
    }

}
