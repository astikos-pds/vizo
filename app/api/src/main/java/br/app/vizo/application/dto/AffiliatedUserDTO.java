package br.app.vizo.application.dto;

import br.app.vizo.core.affiliation.AffiliationStatus;

import java.time.Instant;
import java.util.UUID;

public record AffiliatedUserDTO(
        UUID id,
        UserDTO user,
        MunicipalityDTO municipality,
        String institutionalEmail,
        boolean isAdmin,
        AffiliationStatus status,
        Instant affiliatedAt,
        AffiliatedUserDTO approver,
        Instant approvedAt
) {
}
