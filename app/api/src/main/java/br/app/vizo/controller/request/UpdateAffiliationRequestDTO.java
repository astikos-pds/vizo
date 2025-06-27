package br.app.vizo.controller.request;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

public record UpdateAffiliationRequestDTO(
        AffiliationRequestStatus status
) {
}
