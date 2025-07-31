package br.app.vizo.application.dto;

import br.app.vizo.core.assignment.PermissionMode;

import java.time.Instant;
import java.util.UUID;

public record AssignedUserDTO(
        UUID id,
        AffiliatedUserDTO user,
        DepartmentDTO department,
        PermissionMode permissionMode,
        PermissionPresetDTO permissionPreset,
        PermissionDTO customPermission,
        Instant assignedAt
) {
}
