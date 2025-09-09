package br.app.vizo.application.usecase.affiliation;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AffiliatedUserDTO;
import br.app.vizo.application.exception.AffiliationNotFoundException;
import br.app.vizo.application.mapper.AffiliatedUserMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.affiliation.request.ChangeAffiliationStatusRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.affiliation.AffiliatedUserRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ChangeAffiliationStatusUseCase {

    private final AuthorizationService authorizationService;
    private final AffiliatedUserRepository affiliatedUserRepository;
    private final AffiliatedUserMapper affiliatedUserMapper;

    public AffiliatedUserDTO execute(
            User loggedInUser,
            UUID municipalityId,
            UUID affiliationId,
            ChangeAffiliationStatusRequestDTO request
    ) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        AffiliatedUser targetUser = this.affiliatedUserRepository.findById(affiliationId)
                .orElseThrow(AffiliationNotFoundException::new);

        affiliatedUser.changeStatusOf(targetUser, request.status());

        AffiliatedUser saved = this.affiliatedUserRepository.save(targetUser);
        return this.affiliatedUserMapper.toDto(saved);
    }
}
