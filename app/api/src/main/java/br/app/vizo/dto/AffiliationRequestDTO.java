package br.app.vizo.dto;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

import java.time.Instant;
import java.util.UUID;

public record AffiliationRequestDTO(
        UUID id,
        UserDTO user,
        MunicipalityDTO municipality,
        AffiliationRequestStatus status,
        Instant createdAt,
        UserDTO approver,
        Instant approvedAt
) {
}
