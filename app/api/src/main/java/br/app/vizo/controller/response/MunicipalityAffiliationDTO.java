package br.app.vizo.controller.response;

import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.municipality.Municipality;

public record MunicipalityAffiliationDTO(
        AffiliationRequestDTO affiliationRequest,
        MunicipalityDTO municipality
) {

    public static MunicipalityAffiliationDTO of(AffiliationRequest affiliation, Municipality municipality) {
        return new MunicipalityAffiliationDTO(
                AffiliationRequestDTO.of(affiliation),
                MunicipalityDTO.of(municipality)
        );
    }
}
