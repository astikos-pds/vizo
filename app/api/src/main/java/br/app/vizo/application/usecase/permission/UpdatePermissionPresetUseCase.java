package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.exception.PermissionPresetNotFoundException;
import br.app.vizo.application.exception.PresetNameAlreadyInUseException;
import br.app.vizo.application.mapper.PermissionMapper;
import br.app.vizo.application.mapper.PermissionPresetMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.permission.request.MutatePermissionPresetRequestDTO;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.permission.PermissionPreset;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class UpdatePermissionPresetUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionMapper permissionMapper;
    private final PermissionPresetRepository permissionPresetRepository;
    private final PermissionPresetMapper permissionPresetMapper;

    public PermissionPresetDTO execute(
            User loggedInUser,
            UUID municipalityId,
            UUID permissionPresetId,
            MutatePermissionPresetRequestDTO request
    ) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        boolean nameAlreadyInUse = this.permissionPresetRepository.existsByMunicipalityIdAndName(municipalityId, request.name());
        if (nameAlreadyInUse) {
            throw new PresetNameAlreadyInUseException();
        }

        PermissionPreset permissionPreset = this.permissionPresetRepository.findById(permissionPresetId)
                .orElseThrow(PermissionPresetNotFoundException::new);

        PermissionPreset updated = affiliatedUser.updatePermissionPreset(
                permissionPreset,
                request.name(),
                this.permissionMapper.toModel(request.permission())
        );

        PermissionPreset saved = this.permissionPresetRepository.save(updated);
        return this.permissionPresetMapper.toDto(saved);
    }
}
