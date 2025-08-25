package br.app.vizo.core.assignment.permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionPresetRepository {

    PermissionPreset save(PermissionPreset permissionPreset);

    Optional<PermissionPreset> findById(UUID id);

    List<PermissionPreset> findAllByMunicipalityId(UUID municipalityId);

    void deleteById(UUID id);
}
