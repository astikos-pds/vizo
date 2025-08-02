package br.app.vizo.core.assignment.permission;

import java.util.UUID;

public interface PermissionPresetRepository {

    PermissionPreset save(PermissionPreset permissionPreset);

    Iterable<PermissionPreset> findAllByMunicipalityId(UUID municipalityId);

    void deleteById(UUID id);
}
