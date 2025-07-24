package br.app.vizo.core.shared;

public record Coordinates(
        Double latitude,
        Double longitude
) {

    public static Coordinates of(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }
}
