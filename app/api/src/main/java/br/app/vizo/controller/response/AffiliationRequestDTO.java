package br.app.vizo.controller.response;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

import java.time.Instant;
import java.util.UUID;

public record AffiliationRequestDTO(
        UUID id,
        UUID officialId,
        UUID municipalityId,
        AffiliationRequestStatus status,
        Instant createdAt,
        UUID approvedById,
        Instant approvedAt
) {
}
