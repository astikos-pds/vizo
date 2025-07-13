package br.app.vizo.controller.response;

import br.app.vizo.domain.affiliation.AffiliationRequest;
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

    public static AffiliationRequestDTO of(AffiliationRequest affiliationRequest) {
        UUID approvedById = null;
        if (affiliationRequest.getApprovedBy() != null) {
            approvedById = affiliationRequest.getApprovedBy().getId();
        }

        return new AffiliationRequestDTO(
                affiliationRequest.getId(),
                affiliationRequest.getOfficial().getId(),
                affiliationRequest.getMunicipality().getId(),
                affiliationRequest.getStatus(),
                affiliationRequest.getCreatedAt(),
                approvedById,
                affiliationRequest.getApprovedAt()
        );
    }
}
