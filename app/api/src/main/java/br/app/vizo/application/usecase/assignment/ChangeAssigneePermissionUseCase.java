package br.app.vizo.application.usecase.assignment;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.exception.AssignmentNotFoundException;
import br.app.vizo.application.exception.PermissionPresetNotFoundException;
import br.app.vizo.application.mapper.AssignedUserMapper;
import br.app.vizo.application.mapper.PermissionMapper;
import br.app.vizo.application.service.AuthorizationService;
import br.app.vizo.application.usecase.assignment.request.ChangeAssigneePermissionRequestDTO;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.core.assignment.AssignedUserRepository;
import br.app.vizo.core.assignment.permission.PermissionPreset;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.core.user.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ChangeAssigneePermissionUseCase {

    private final AuthorizationService authorizationService;
    private final AssignedUserRepository assignedUserRepository;
    private final PermissionMapper permissionMapper;
    private final PermissionPresetRepository permissionPresetRepository;
    private final AssignedUserMapper assignedUserMapper;

    public AssignedUserDTO execute(
            User user,
            UUID municipalityId,
            UUID departmentId,
            UUID assignmentId,
            ChangeAssigneePermissionRequestDTO request
    ) {
        AssignedUser assignedUser = this.authorizationService.ensureUserIsAssignedTo(user, municipalityId, departmentId);

        AssignedUser target = this.assignedUserRepository.findById(assignmentId)
                .orElseThrow(AssignmentNotFoundException::new);

        PermissionPreset permissionPreset = this.permissionPresetRepository.findById(request.permissionPresetId())
                .orElseThrow(PermissionPresetNotFoundException::new);

        AssignedUser updated = assignedUser.changePermissionOf(
                target,
                request.permissionMode(),
                this.permissionMapper.toModel(request.customPermission()),
                permissionPreset
        );

        AssignedUser saved = this.assignedUserRepository.save(updated);
        return this.assignedUserMapper.toDto(saved);
    }
}
