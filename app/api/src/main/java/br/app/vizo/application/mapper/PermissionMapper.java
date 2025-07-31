package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.PermissionDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.assignment.Permission;
import br.app.vizo.infrastructure.persistence.jpa.entity.PermissionEntity;

@Mapper
public class PermissionMapper implements RepresentationMapper<Permission, PermissionEntity, PermissionDTO> {

    @Override
    public PermissionDTO toDto(Permission permission) {
        return new PermissionDTO(
                permission.canViewReports(),
                permission.canUpdateStatus(),
                permission.canManageUsers()
        );
    }

    @Override
    public PermissionEntity toEntity(Permission permission) {
        return new PermissionEntity(
                permission.canViewReports(),
                permission.canUpdateStatus(),
                permission.canManageUsers()
        );
    }

    @Override
    public Permission toModel(PermissionEntity entity) {
        return new Permission(
                entity.canViewReports(),
                entity.canUpdateStatus(),
                entity.canManageUsers()
        );
    }
}
