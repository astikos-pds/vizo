package br.app.vizo.application.usecase.report.request;

import java.util.Set;

public record ReportProblemRequestDTO(
        String description,
        Set<String> imagesUrls,
        Double latitude,
        Double longitude
) {
}
