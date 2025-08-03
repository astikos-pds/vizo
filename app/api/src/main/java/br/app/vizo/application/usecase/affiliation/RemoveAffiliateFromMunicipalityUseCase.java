package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.AffiliationNotFoundException;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.affiliation.exception.SelfActionNotAllowedException;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class RemoveAffiliateFromMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final AffiliatedUserRepository affiliatedUserRepository;

    public void execute(User loggedInUser, UUID municipalityId, UUID targetUserId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        AffiliatedUser targetUser = this.affiliatedUserRepository.findById(targetUserId)
                .orElseThrow(AffiliationNotFoundException::new);

        if (affiliatedUser.isSameAs(targetUser)) {
            throw new SelfActionNotAllowedException();
        }

        this.affiliatedUserRepository.deleteById(targetUserId);
    }
}
