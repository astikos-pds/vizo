package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.exception.PermissionPresetDoesNotBelongToMunicipalityException;
import br.app.vizo.application.exception.PermissionPresetNotFoundException;
import br.app.vizo.application.mapper.PermissionPresetMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.permission.PermissionPreset;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetPermissionPresetInMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionPresetRepository permissionPresetRepository;
    private final PermissionPresetMapper permissionPresetMapper;

    public PermissionPresetDTO execute(User loggedInUser, UUID municipalityId, UUID permissionPresetId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        PermissionPreset permissionPreset = this.permissionPresetRepository.findById(permissionPresetId)
                .orElseThrow(PermissionPresetNotFoundException::new);

        if (!permissionPreset.belongsTo(affiliatedUser.getMunicipality())) {
            throw new PermissionPresetDoesNotBelongToMunicipalityException();
        }

        return this.permissionPresetMapper.toDto(permissionPreset);
    }
}
