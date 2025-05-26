package br.app.vizo.controller.response;

import java.time.Instant;
import java.util.UUID;

public record ReportDTO(
        UUID id,
        String description,
        String imageUrl,
        Double latitude,
        Double longitude,
        UUID citizenId,
        UUID problemId,
        Instant createdAt
) {
}
