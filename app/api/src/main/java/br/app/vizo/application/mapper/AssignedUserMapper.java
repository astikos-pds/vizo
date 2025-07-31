package br.app.vizo.application.mapper;

import br.app.vizo.application.Mapper;
import br.app.vizo.application.dto.AssignedUserDTO;
import br.app.vizo.application.mapper.base.RepresentationMapper;
import br.app.vizo.core.assignment.AssignedUser;
import br.app.vizo.infrastructure.persistence.jpa.entity.AssignedUserEntity;

@Mapper
public class AssignedUserMapper implements
        RepresentationMapper<AssignedUser, AssignedUserEntity, AssignedUserDTO>
{
    private final AffiliatedUserMapper affiliatedUserMapper;
    private final DepartmentMapper departmentMapper;
    private final PermissionPresetMapper permissionPresetMapper;
    private final PermissionMapper permissionMapper;


    public AssignedUserMapper(AffiliatedUserMapper affiliatedUserMapper, DepartmentMapper departmentMapper, PermissionPresetMapper permissionPresetMapper, PermissionMapper permissionMapper) {
        this.affiliatedUserMapper = affiliatedUserMapper;
        this.departmentMapper = departmentMapper;
        this.permissionPresetMapper = permissionPresetMapper;
        this.permissionMapper = permissionMapper;
    }


    @Override
    public AssignedUserDTO toDto(AssignedUser assignedUser) {
        return new AssignedUserDTO(
                assignedUser.getId(),
                this.affiliatedUserMapper.toDto(assignedUser.getUser()),
                this.departmentMapper.toDto(assignedUser.getDepartment()),
                assignedUser.getPermissionMode(),
                this.permissionPresetMapper.toDto(assignedUser.getPermissionPreset()),
                this.permissionMapper.toDto(assignedUser.getCustomPermission()),
                assignedUser.getAssignedAt()
        );
    }

    @Override
    public AssignedUserEntity toEntity(AssignedUser assignedUser) {
        return new AssignedUserEntity(
                assignedUser.getId(),
                this.affiliatedUserMapper.toEntity(assignedUser.getUser()),
                this.departmentMapper.toEntity(assignedUser.getDepartment()),
                assignedUser.getPermissionMode(),
                this.permissionPresetMapper.toEntity(assignedUser.getPermissionPreset()),
                this.permissionMapper.toEntity(assignedUser.getCustomPermission()),
                assignedUser.getAssignedAt()
        );
    }

    @Override
    public AssignedUser toModel(AssignedUserEntity entity) {
        return new AssignedUser(
                entity.getId(),
                this.affiliatedUserMapper.toModel(entity.getUser()),
                this.departmentMapper.toModel(entity.getDepartment()),
                entity.getPermissionMode(),
                this.permissionPresetMapper.toModel(entity.getPermissionPreset()),
                this.permissionMapper.toModel(entity.getCustomPermission()),
                entity.getAssignedAt()
        );
    }
}
