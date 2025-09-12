package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.permission.params.ExistsPermissionPresetParams;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ExistsPermissionPresetByParamsUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionPresetRepository permissionPresetRepository;

    public boolean execute(User loggedInUser, UUID municipalityId, ExistsPermissionPresetParams params) {
        this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        return this.permissionPresetRepository.existsByMunicipalityIdAndName(municipalityId, params.getName());
    }
}
