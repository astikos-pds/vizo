package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.exception.PermissionPresetNotFoundException;
import br.app.vizo.application.mapper.PermissionPresetMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.permission.params.GetPermissionPresetParams;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetPermissionPresetByParamsUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionPresetRepository permissionPresetRepository;
    private final PermissionPresetMapper permissionPresetMapper;

    public PermissionPresetDTO execute(User loggedInUser, UUID municipalityId, GetPermissionPresetParams params) {
        this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        return this.permissionPresetRepository.findByMunicipalityIdAndName(municipalityId, params.getName())
                .map(this.permissionPresetMapper::toDto)
                .orElseThrow(PermissionPresetNotFoundException::new);
    }
}
