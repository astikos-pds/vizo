package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.PermissionPresetDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.assignment.PermissionPreset;
import br.app.vizo.core.shared.Name;
import br.app.vizo.infrastructure.persistence.jpa.entity.PermissionPresetEntity;

@Mapper
public class PermissionPresetMapper implements
        RepresentationMapper<PermissionPreset, PermissionPresetEntity, PermissionPresetDTO>
{
    private final MunicipalityMapper municipalityMapper;
    private final PermissionMapper permissionMapper;

    public PermissionPresetMapper(MunicipalityMapper municipalityMapper, PermissionMapper permissionMapper) {
        this.municipalityMapper = municipalityMapper;
        this.permissionMapper = permissionMapper;
    }


    @Override
    public PermissionPresetDTO toDto(PermissionPreset permissionPreset) {
        return new PermissionPresetDTO(
                permissionPreset.getId(),
                this.municipalityMapper.toDto(permissionPreset.getMunicipality()),
                permissionPreset.getName(),
                this.permissionMapper.toDto(permissionPreset.getPermission())
        );
    }

    @Override
    public PermissionPresetEntity toEntity(PermissionPreset permissionPreset) {
        return new PermissionPresetEntity(
                permissionPreset.getId(),
                this.municipalityMapper.toEntity(permissionPreset.getMunicipality()),
                permissionPreset.getName(),
                this.permissionMapper.toEntity(permissionPreset.getPermission())
        );
    }

    @Override
    public PermissionPreset toModel(PermissionPresetEntity entity) {
        return new PermissionPreset(
                entity.getId(),
                this.municipalityMapper.toModel(entity.getMunicipality()),
                new Name(entity.getName()),
                this.permissionMapper.toModel(entity.getPermission())
        );
    }
}
