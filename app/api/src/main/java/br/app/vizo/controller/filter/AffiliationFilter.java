package br.app.vizo.controller.filter;

import br.app.vizo.domain.affiliation.AffiliationStatus;

import java.util.Optional;

public record AffiliationFilter(
        Optional<AffiliationStatus> status
) {
}
