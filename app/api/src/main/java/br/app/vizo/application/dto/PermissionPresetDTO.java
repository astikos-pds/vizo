package br.app.vizo.application.dto;

import java.util.UUID;

public record PermissionPresetDTO(
        UUID id,
        MunicipalityDTO municipality,
        String name,
        PermissionDTO permission
) {
}
