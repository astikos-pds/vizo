package br.app.vizo.controller.request;

import java.util.List;

public record CreateReportRequestDTO(
        String description,
        List<String> imagesUrls,
        Double latitude,
        Double longitude
) {
}
