package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.CannotLeaveAsOnlyMemberException;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class DisaffiliateFromMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final AffiliatedUserRepository affiliatedUserRepository;

    public void execute(User loggedInUser, UUID municipalityId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            this.deleteUserAffiliationFromMunicipality(loggedInUser, municipalityId);
            return;
        }

        long numberOfAdminsInMunicipality = this.affiliatedUserRepository.countByMunicipalityIdAndIsAdmin(municipalityId, true);
        if (numberOfAdminsInMunicipality > 1) {
            this.deleteUserAffiliationFromMunicipality(loggedInUser, municipalityId);
            return;
        }

        AffiliatedUser firstUserAffiliatedAfterAdmin = this.affiliatedUserRepository
                .findFirstApprovedNonAdminByMunicipalityId(municipalityId)
                .orElseThrow(CannotLeaveAsOnlyMemberException::new);

        affiliatedUser.promote(firstUserAffiliatedAfterAdmin);
        this.affiliatedUserRepository.save(firstUserAffiliatedAfterAdmin);

        this.deleteUserAffiliationFromMunicipality(loggedInUser, municipalityId);
    }

    private void deleteUserAffiliationFromMunicipality(User loggedInUser, UUID municipalityId) {
        this.affiliatedUserRepository.deleteByUserIdAndMunicipalityId(loggedInUser.getId(), municipalityId);
    }
}
