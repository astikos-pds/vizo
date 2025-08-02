package br.app.vizo.application.usecase.report.request;

import br.app.vizo.core.problem.ProblemType;

import java.util.Set;

public record ReportProblemRequestDTO(
        String description,
        Set<String> imagesUrls,
        Double latitude,
        Double longitude,
        ProblemType problemType
) {
}
