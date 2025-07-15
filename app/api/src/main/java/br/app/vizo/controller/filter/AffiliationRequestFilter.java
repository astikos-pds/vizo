package br.app.vizo.controller.filter;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

public record AffiliationRequestFilter(
        AffiliationRequestStatus status
) {
}
