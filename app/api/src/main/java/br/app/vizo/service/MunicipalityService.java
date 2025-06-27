package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.repository.MunicipalityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final AffiliationService affiliationService;

    public MunicipalityService(MunicipalityRepository municipalityRepository, AffiliationService affiliationService) {
        this.municipalityRepository = municipalityRepository;
        this.affiliationService = affiliationService;
    }

    public AffiliationRequestDTO updateMunicipalityAffiliation(
            String municipalityId,
            String affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        boolean municipalityExists = this.municipalityRepository.existsById(UUID.fromString(municipalityId));
        if (!municipalityExists) {
            throw new NotFoundException("Municipality not found.");
        }

        return this.affiliationService.updateAffiliation(affiliationId, body, authentication);
    }
}
