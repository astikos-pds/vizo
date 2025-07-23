package br.app.vizo.controller.request;

import br.app.vizo.domain.affiliation.AffiliationStatus;

public record UpdateAffiliationDTO(
        AffiliationStatus status
) {
}
