package br.app.vizo.service;

import br.app.vizo.controller.request.UpdateAffiliationRequestDTO;
import br.app.vizo.controller.response.AffiliationRequestDTO;
import br.app.vizo.domain.affiliation.AffiliationRequest;
import br.app.vizo.domain.user.Official;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.AffiliationRequestMapper;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.OfficialRepository;
import br.app.vizo.util.DateUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AffiliationService {

    private final AffiliationRequestRepository affiliationRequestRepository;
    private final OfficialRepository officialRepository;
    private final AffiliationRequestMapper affiliationRequestMapper;

    public AffiliationService(
            AffiliationRequestRepository affiliationRequestRepository,
            OfficialRepository officialRepository,
            AffiliationRequestMapper affiliationRequestMapper
    ) {
        this.affiliationRequestRepository = affiliationRequestRepository;
        this.officialRepository = officialRepository;
        this.affiliationRequestMapper = affiliationRequestMapper;
    }

    public AffiliationRequestDTO updateAffiliation(
            String id,
            UpdateAffiliationRequestDTO body,
            Authentication authentication
    ) {
        Official loggedInOfficial = this.officialRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        UUID affiliationId = UUID.fromString(id);
        AffiliationRequest affiliationRequest = this.affiliationRequestRepository.findById(affiliationId).orElseThrow(
                () -> new NotFoundException("Affiliation request not found.")
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
