package br.app.vizo.dto;

import br.app.vizo.domain.affiliation.AffiliationStatus;

import java.time.Instant;
import java.util.UUID;

public record AffiliationDTO(
        UUID id,
        UserDTO user,
        MunicipalityDTO municipality,
        AffiliationStatus status,
        Instant createdAt,
        UserDTO approver,
        Instant approvedAt
) {
}
