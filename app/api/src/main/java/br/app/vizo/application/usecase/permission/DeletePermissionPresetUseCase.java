package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class DeletePermissionPresetUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionPresetRepository permissionPresetRepository;

    public void execute(User loggedInUser, UUID municipalityId, UUID permissionPresetId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        this.permissionPresetRepository.deleteById(permissionPresetId);
    }
}
