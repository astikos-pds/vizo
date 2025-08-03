package br.app.vizo.core.assignment.permission;

import java.util.List;
import java.util.UUID;

public interface PermissionPresetRepository {

    PermissionPreset save(PermissionPreset permissionPreset);

    List<PermissionPreset> findAllByMunicipalityId(UUID municipalityId);

    void deleteById(UUID id);
}
