package br.app.vizo.dto;

import br.app.vizo.domain.user.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReportDTO(
        UUID id,
        String description,
        List<ReportImageDTO> images,
        Double latitude,
        Double longitude,
        User user,
        ProblemDTO problem,
        Instant createdAt
) {
}
