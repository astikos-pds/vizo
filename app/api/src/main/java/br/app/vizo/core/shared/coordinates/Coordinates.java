package br.app.vizo.core.shared.coordinates;

import br.app.vizo.core.shared.exception.InvalidCoordinatesException;

public record Coordinates(
        Latitude latitude,
        Longitude longitude
) {

    public Coordinates {
        if (latitude == null || longitude == null) {
            throw new InvalidCoordinatesException();
        }
    }

    public static Coordinates of(Double latitude, Double longitude) {
        return new Coordinates(
                new Latitude(latitude), new Longitude(longitude)
        );
    }

    public Double getLatitude() {
        return latitude.value();
    }

    public Double getLongitude() {
        return longitude.value();
    }

}
