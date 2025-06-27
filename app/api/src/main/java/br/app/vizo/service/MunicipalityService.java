package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.user.Official;
import br.app.vizo.domain.user.OfficialRole;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<AffiliationRequestDTO> getMunicipalityAffiliations(
            String municipalityIdRaw,
            Pageable pageable,
            Authentication authentication
    ) {
        UUID municipalityId = UUID.fromString(municipalityIdRaw);

        this.checkIfOfficialIsAuthorized(municipalityId, authentication.getName());

        return this.affiliationRequestRepository
                .findAllByMunicipalityId(municipalityId, pageable)
                .map(this.affiliationRequestMapper::toDto);
    }

    public AffiliationRequestDTO updateMunicipalityAffiliation(
            String municipalityIdRaw,
            String affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.checkIfOfficialIsAuthorized(
                UUID.fromString(municipalityIdRaw),
                authentication.getName()
        );

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

    private Official checkIfOfficialIsAuthorized(UUID municipalityId, String officialDocument) {
        boolean municipalityExists = this.municipalityRepository.existsById(municipalityId);
        if (!municipalityExists) {
            throw new NotFoundException("Municipality not found.");
        }

        Official loggedInOfficial = this.officialRepository.findByDocument(officialDocument).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        if (!loggedInOfficial.getMunicipality().getId().equals(municipalityId)
                || loggedInOfficial.getRole() != OfficialRole.ADMIN
        ) {
            throw new ForbiddenException("Permission denied to update affiliation request.");
        }

        return loggedInOfficial;
    }
}
