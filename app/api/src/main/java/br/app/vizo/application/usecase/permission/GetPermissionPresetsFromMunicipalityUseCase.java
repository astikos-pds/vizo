package br.app.vizo.application.usecase.permission;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.exception.MustBeAdminException;
import br.app.vizo.application.mapper.PermissionPresetMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.core.affiliation.AffiliatedUser;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetPermissionPresetsFromMunicipalityUseCase {

    private final AuthorizationService authorizationService;
    private final PermissionPresetRepository permissionPresetRepository;
    private final PermissionPresetMapper permissionPresetMapper;

    public List<PermissionPresetDTO> execute(User loggedInUser, UUID municipalityId) {
        AffiliatedUser affiliatedUser = this.authorizationService.ensureUserIsAffiliatedTo(loggedInUser, municipalityId);

        if (!affiliatedUser.isAdmin()) {
            throw new MustBeAdminException();
        }

        return this.permissionPresetRepository.findAllByMunicipalityId(municipalityId)
                .stream()
                .map(this.permissionPresetMapper::toDto)
                .toList();
    }
}
