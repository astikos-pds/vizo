package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationFilter;
import br.app.vizo.controller.request.UpdateAffiliationDTO;
import br.app.vizo.domain.user.User;
import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.domain.affiliation.Affiliation;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.mapper.AffiliationMapper;
import br.app.vizo.repository.AffiliationRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AffiliationService {

    private final AffiliationRepository affiliationRepository;
    private final AffiliationMapper affiliationMapper;

    private final OfficialService officialService;

    public AffiliationService(
            AffiliationRepository affiliationRepository,
            AffiliationMapper affiliationMapper,
            OfficialService officialService
    ) {
        this.affiliationRepository = affiliationRepository;
        this.affiliationMapper = affiliationMapper;
        this.officialService = officialService;
    }

    public Page<AffiliationDTO> getAffiliations(
            UUID municipalityId,
            AffiliationFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        if (filter.status().isEmpty()) {
            return this.affiliationRepository
                    .findAllByMunicipalityId(municipalityId, pageable)
                    .map(this.affiliationMapper::toDto);
        }

        return this.affiliationRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, filter.status().get(), pageable)
                .map(this.affiliationMapper::toDto);
    }

    public AffiliationDTO updateAffiliation(
            UUID municipalityId,
            UUID affiliationId,
            UpdateAffiliationDTO body,
            Authentication authentication
    ) {
        User loggedInUser = this.officialService
                .getAuthorizedAdminContext(municipalityId, authentication)
                .loggedInUser();

        Affiliation affiliation = this.affiliationRepository
                .findById(affiliationId)
                .orElseThrow(() -> new NotFoundException("Affiliation request not found."));

        affiliation.setStatus(body.status());
        affiliation.setApprover(loggedInUser);
        affiliation.setApprovedAt(DateUtil.now());

        Affiliation saved = this.affiliationRepository.save(affiliation);

        return this.affiliationMapper.toDto(saved);
    }

    public void deleteAffiliation(
            UUID municipalityId,
            UUID affiliationId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.affiliationRepository.deleteById(affiliationId);
    }
}
