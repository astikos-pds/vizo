package br.app.vizo.service;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.Official;
import br.app.vizo.dto.OfficialContextDTO;
import br.app.vizo.exception.http.ForbiddenException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.OfficialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficialService {

    private final OfficialRepository officialRepository;
    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;

    public OfficialContextDTO getAuthorizedCommonContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, false);
    }

    public OfficialContextDTO getAuthorizedAdminContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, true);
    }

    private OfficialContextDTO getAuthorizedOfficialContext(
            UUID municipalityId,
            Authentication authentication,
            boolean isForAdmins
    ) {
        Municipality municipality = this.municipalityRepository.findById(municipalityId).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        Official loggedInOfficial = this.officialRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("Official not found.")
        );

        validateOfficialAccess(municipality, loggedInOfficial, isForAdmins);

        return new OfficialContextDTO(municipality, loggedInOfficial);
    }

    public void validateOfficialAccess(Municipality municipality, Official official, boolean isForAdmins) {
        if (!official.isAdmin()) {
            boolean officialBelongsToMunicipality = this.affiliationRequestRepository
                    .existsByMunicipalityIdAndOfficialIdAndStatus(
                            municipality.getId(),
                            official.getId(),
                            AffiliationRequestStatus.APPROVED
                    );

            if (!officialBelongsToMunicipality) {
                throw new ForbiddenException("Official does not belong to this municipality.");
            }
        }

        if (isForAdmins && !official.isAdmin()) {
            throw new ForbiddenException("Only admins are allowed to perform this action.");
        }
    }
}
