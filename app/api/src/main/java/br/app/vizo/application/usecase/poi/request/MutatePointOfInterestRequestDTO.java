package br.app.vizo.application.usecase.poi.request;

public record MutatePointOfInterestRequestDTO(
        String name,
        Double latitude,
        Double longitude,
        Double radius,
        String colorHex,
        boolean active
) {
}
