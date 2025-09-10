package br.app.vizo.application.usecase.me.filter;

import br.app.vizo.core.affiliation.AffiliationStatus;

public record GetMyAffiliationsParams(
        AffiliationStatus status
) {
}
