package br.app.vizo.controller.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReportDTO(
        UUID id,
        String description,
        List<ReportImageDTO> images,
        Double latitude,
        Double longitude,
        CitizenDTO citizen,
        ProblemDTO problem,
        Instant createdAt
) {
}
