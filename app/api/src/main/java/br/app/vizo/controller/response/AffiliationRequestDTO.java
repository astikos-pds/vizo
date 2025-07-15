package br.app.vizo.controller.response;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

import java.time.Instant;
import java.util.UUID;

public record AffiliationRequestDTO(
        UUID id,
        OfficialDTO official,
        MunicipalityDTO municipality,
        AffiliationRequestStatus status,
        Instant createdAt,
        OfficialDTO approvedBy,
        Instant approvedAt
) {
}
