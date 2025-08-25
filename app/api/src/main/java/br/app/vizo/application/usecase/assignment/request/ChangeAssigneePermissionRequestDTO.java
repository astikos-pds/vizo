package br.app.vizo.application.usecase.assignment.request;

import br.app.vizo.application.dto.PermissionDTO;
import br.app.vizo.core.assignment.permission.PermissionMode;

import java.util.UUID;

public record ChangeAssigneePermissionRequestDTO(
        PermissionMode permissionMode,
        PermissionDTO customPermission,
        UUID permissionPresetId
) {
}
