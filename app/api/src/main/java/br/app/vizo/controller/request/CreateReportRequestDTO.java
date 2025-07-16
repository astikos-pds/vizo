package br.app.vizo.controller.request;

import br.app.vizo.domain.problem.ProblemType;

import java.util.List;

public record CreateReportRequestDTO(
        String description,
        List<String> imagesUrls,
        Double latitude,
        Double longitude,
        ProblemType problemType
) {
}
