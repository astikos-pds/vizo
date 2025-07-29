package br.app.vizo.application.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ReportDTO(
        UUID id,
        UserDTO user,
        ProblemDTO problem,
        String description,
        Double latitude,
        Double longitude,
        Set<String> imagesUrls,
        Double credibility,
        Instant createdAt
) {
}
