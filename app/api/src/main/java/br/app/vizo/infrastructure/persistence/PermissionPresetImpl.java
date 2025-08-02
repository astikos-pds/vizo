package br.app.vizo.infrastructure.persistence;

import br.app.vizo.application.mapper.PermissionPresetMapper;
import br.app.vizo.core.assignment.permission.PermissionPreset;
import br.app.vizo.core.assignment.permission.PermissionPresetRepository;
import br.app.vizo.infrastructure.persistence.jpa.entity.PermissionPresetEntity;
import br.app.vizo.infrastructure.persistence.jpa.repository.PermissionPresetJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PermissionPresetImpl implements PermissionPresetRepository {

    private final PermissionPresetJpaRepository jpaRepository;
    private final PermissionPresetMapper mapper;

    public PermissionPresetImpl(PermissionPresetJpaRepository jpaRepository, PermissionPresetMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PermissionPreset save(PermissionPreset permissionPreset) {
        PermissionPresetEntity entity = this.mapper.toEntity(permissionPreset);
        PermissionPresetEntity saved = this.jpaRepository.save(entity);
        return this.mapper.toModel(saved);
    }

    @Override
    public Iterable<PermissionPreset> findAllByMunicipalityId(UUID municipalityId) {
        return this.jpaRepository.findAllByMunicipalityId(municipalityId).stream().map(this.mapper::toModel).toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaRepository.deleteById(id);
    }

}
