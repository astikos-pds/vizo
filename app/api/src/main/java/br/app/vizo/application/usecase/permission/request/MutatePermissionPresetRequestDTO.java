package br.app.vizo.application.usecase.permission.request;

import br.app.vizo.application.dto.PermissionDTO;

public record MutatePermissionPresetRequestDTO(
        String name,
        PermissionDTO permission
) {
}
