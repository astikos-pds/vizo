package br.app.vizo.application.usecase.affiliation.request;

import br.app.vizo.core.affiliation.AffiliationStatus;

public record ChangeAffiliationStatusRequestDTO(AffiliationStatus status) {
}
