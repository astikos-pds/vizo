package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationRequestFilter;
import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AffiliationService {

    private final AffiliationRequestRepository affiliationRequestRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;

    private final OfficialRepository officialRepository;
    private final OfficialService officialService;

    public AffiliationService(
            AffiliationRequestRepository affiliationRequestRepository,
            AffiliationRequestMapper affiliationRequestMapper,
            OfficialRepository officialRepository,
            OfficialService officialService
    ) {
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
        this.officialRepository = officialRepository;
        this.officialService = officialService;
    }

    public Page<AffiliationRequestDTO> getAffiliations(
            UUID municipalityId,
            AffiliationRequestFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        if (filter.status().isEmpty()) {
            return this.affiliationRequestRepository
                    .findAllByMunicipalityId(municipalityId, pageable)
                    .map(this.affiliationRequestMapper::toDto);
        }

        return this.affiliationRequestRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, filter.status().get(), pageable)
                .map(this.affiliationRequestMapper::toDto);
    }

    public AffiliationRequestDTO updateAffiliation(
            UUID municipalityId,
            UUID affiliationId,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.officialService
                .getAuthorizedAdminContext(municipalityId, authentication)
                .loggedInOfficial();

        AffiliationRequest affiliationRequest = this.affiliationRequestRepository
                .findById(affiliationId)
                .orElseThrow(() -> new NotFoundException("Affiliation request not found."));

        affiliationRequest.setStatus(body.status());
        affiliationRequest.setApprovedBy(loggedInOfficial);
        affiliationRequest.setApprovedAt(DateUtil.now());

        Official official = affiliationRequest.getOfficial();
        official.setWasApproved(body.status() == AffiliationRequestStatus.APPROVED);
        this.officialRepository.save(official);

        AffiliationRequest saved = this.affiliationRequestRepository.save(affiliationRequest);

        return this.affiliationRequestMapper.toDto(saved);
    }

    public void deleteAffiliation(
            UUID municipalityId,
            UUID affiliationId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.affiliationRequestRepository.deleteById(affiliationId);
    }
}
