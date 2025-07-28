package br.app.vizo.service;

import br.app.vizo.controller.filter.AffiliationFilter;
import br.app.vizo.controller.request.CreateAffiliationRequestDTO;
import br.app.vizo.controller.request.UpdateAffiliationDTO;
import br.app.vizo.domain.affiliation.AffiliationStatus;
import br.app.vizo.domain.user.User;
import br.app.vizo.dto.AffiliatedUserContextDTO;
import br.app.vizo.dto.AffiliationDTO;
import br.app.vizo.domain.affiliation.Affiliation;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.exception.UnprocessableEntityException;
import br.app.vizo.mapper.OldAffiliationMapper;
import br.app.vizo.repository.OldAffiliationRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AffiliationService {

    private final OldAffiliationRepository oldAffiliationRepository;
    private final OldAffiliationMapper oldAffiliationMapper;

    private final OfficialService officialService;

    public AffiliationService(
            OldAffiliationRepository oldAffiliationRepository,
            OldAffiliationMapper oldAffiliationMapper,
            OfficialService officialService
    ) {
        this.oldAffiliationRepository = oldAffiliationRepository;
        this.oldAffiliationMapper = oldAffiliationMapper;
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
            return this.oldAffiliationRepository
                    .findAllByMunicipalityId(municipalityId, pageable)
                    .map(this.oldAffiliationMapper::toDto);
        }

        return this.oldAffiliationRepository
                .findAllByMunicipalityIdAndStatus(municipalityId, filter.status().get(), pageable)
                .map(this.oldAffiliationMapper::toDto);
    }

    public AffiliationDTO createAffiliation(
            UUID municipalityId,
            CreateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        AffiliatedUserContextDTO context = this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        String institutionalDomain = body.institutionalEmail().split("@")[1];

        if (!institutionalDomain.equals(context.municipality().getEmailDomain())) {
            throw new UnprocessableEntityException("Institutional domain doesn't match municipality's.");
        }

        Affiliation affiliation = new Affiliation();
        affiliation.setUser(context.loggedInUser());
        affiliation.setMunicipality(context.municipality());
        affiliation.setInstitutionalEmail(body.institutionalEmail());
        affiliation.setStatus(AffiliationStatus.PENDING);

        Affiliation saved = this.oldAffiliationRepository.save(affiliation);

        return this.oldAffiliationMapper.toDto(saved);
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

        Affiliation affiliation = this.oldAffiliationRepository
                .findById(affiliationId)
                .orElseThrow(() -> new NotFoundException("Affiliation request not found."));

        affiliation.setStatus(body.status());
        affiliation.setApprover(loggedInUser);
        affiliation.setApprovedAt(DateUtil.now());

        Affiliation saved = this.oldAffiliationRepository.save(affiliation);

        return this.oldAffiliationMapper.toDto(saved);
    }

    public void deleteAffiliation(
            UUID municipalityId,
            UUID affiliationId,
            Authentication authentication
    ) {
        this.officialService.getAuthorizedCommonContext(municipalityId, authentication);

        this.oldAffiliationRepository.deleteById(affiliationId);
    }
}
