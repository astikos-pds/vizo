package br.app.vizo.controller.request;

public record CreateReportRequestDTO(
        String description,
        String imageUrl,
        Double latitude,
        Double longitude
) {
}
