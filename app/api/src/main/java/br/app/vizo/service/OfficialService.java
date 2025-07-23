package br.app.vizo.service;

import br.app.vizo.domain.affiliation.AffiliationRequestStatus;
import br.app.vizo.domain.municipality.Municipality;
import br.app.vizo.domain.user.User;
import br.app.vizo.dto.AffiliatedUserContextDTO;
import br.app.vizo.exception.ForbiddenException;
import br.app.vizo.exception.NotFoundException;
import br.app.vizo.repository.AffiliationRequestRepository;
import br.app.vizo.repository.MunicipalityRepository;
import br.app.vizo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficialService {

    private final UserRepository userRepository;
    private final MunicipalityRepository municipalityRepository;
    private final AffiliationRequestRepository affiliationRequestRepository;

    public AffiliatedUserContextDTO getAuthorizedCommonContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, false);
    }

    public AffiliatedUserContextDTO getAuthorizedAdminContext(UUID municipalityId, Authentication authentication) {
        return getAuthorizedOfficialContext(municipalityId, authentication, true);
    }

    private AffiliatedUserContextDTO getAuthorizedOfficialContext(
            UUID municipalityId,
            Authentication authentication,
            boolean isForAdmins
    ) {
        Municipality municipality = this.municipalityRepository.findById(municipalityId).orElseThrow(
                () -> new NotFoundException("Municipality not found.")
        );

        User loggedInUser = this.userRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        validateOfficialAccess(municipality, loggedInUser, isForAdmins);

        return new AffiliatedUserContextDTO(municipality, loggedInUser);
    }

    public void validateOfficialAccess(Municipality municipality, User user, boolean isForAdmins) {
//        if (!official.isAdmin()) {
//            boolean officialBelongsToMunicipality = this.affiliationRequestRepository
//                    .existsByMunicipalityIdAndOfficialIdAndStatus(
//                            municipality.getId(),
//                            user.getId(),
//                            AffiliationRequestStatus.APPROVED
//                    );
//
//            if (!officialBelongsToMunicipality) {
//                throw new ForbiddenException("Official does not belong to this municipality.");
//            }
//        }
//
//        if (isForAdmins && !official.isAdmin()) {
//            throw new ForbiddenException("Only admins are allowed to perform this action.");
//        }

        boolean officialBelongsToMunicipality = this.affiliationRequestRepository
                .existsByMunicipalityIdAndUserIdAndStatus(
                        municipality.getId(),
                        user.getId(),
                        AffiliationRequestStatus.APPROVED
                );

        if (!officialBelongsToMunicipality) {
            throw new ForbiddenException("Official does not belong to this municipality.");
        }
    }
}
