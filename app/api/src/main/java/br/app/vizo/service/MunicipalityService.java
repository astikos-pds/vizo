package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            AffiliationRequestMapper affiliationRequestMapper
    ) {
        this.municipalityRepository = municipalityRepository;
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
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

        Official loggedInOfficial = this.officialRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        if (!loggedInOfficial.getMunicipality().getId().equals(UUID.fromString(municipalityId))) {
            throw new ForbiddenException("Permission denied to update affiliation request.");
        }

        AffiliationRequest affiliationRequest = this.affiliationRequestRepository
                .findById(UUID.fromString(affiliationId))
                .orElseThrow(() -> new NotFoundException("Affiliation request not found.")
        );

        affiliationRequest.setStatus(body.status());
        affiliationRequest.setApprovedBy(loggedInOfficial);
        affiliationRequest.setApprovedAt(DateUtil.now());

        Official official = affiliationRequest.getOfficial();
        official.setWasApproved(true);
        this.officialRepository.save(official);

        return this.affiliationRequestMapper.toDto(this.affiliationRequestRepository.save(affiliationRequest));
    }
}
