package br.app.vizo.dto;

import java.time.Instant;
import java.util.UUID;

public record MunicipalityDTO(
        UUID id,
        String name,
        String emailDomain,
        String iconUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
