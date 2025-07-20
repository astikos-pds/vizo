package br.app.vizo.controller.filter;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;

import java.util.Optional;

public record AffiliationRequestFilter(
        Optional<AffiliationRequestStatus> status
) {
}
