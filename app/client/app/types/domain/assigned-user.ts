import type {
  AffiliatedUser,
  AffiliatedUserDTO,
  AffiliatedUserMapper,
} from "./affiliated-user";
import type { Department, DepartmentDTO, DepartmentMapper } from "./department";
import type {
  Permission,
  PermissionMode,
  PermissionPreset,
  PermissionPresetDTO,
  PermissionPresetMapper,
} from "./permission";

export interface AssignedUserDTO {
  id: string;
  user: AffiliatedUserDTO;
  department: DepartmentDTO;
  permissionMode: PermissionMode;
  permissionPreset: PermissionPresetDTO | null;
  customPermission: Permission;
  assignedAt: string;
}

export interface AssignedUser {
  id: string;
  user: AffiliatedUser;
  department: Department;
  permissionMode: PermissionMode;
  permissionPreset: PermissionPreset | null;
  customPermission: Permission;
  assignedAt: Date;
}

export class AssignedUserMapper {
  constructor(
    private readonly affiliatedUserMapper: AffiliatedUserMapper,
    private readonly departmentMapper: DepartmentMapper,
    private readonly permissionPresetMapper: PermissionPresetMapper
  ) {}

  public toModel(dto: AssignedUserDTO): AssignedUser {
    return {
      ...dto,
      user: this.affiliatedUserMapper.toModel(dto.user),
      department: this.departmentMapper.toModel(dto.department),
      permissionPreset: dto.permissionPreset
        ? this.permissionPresetMapper.toModel(dto.permissionPreset)
        : null,
      assignedAt: new Date(dto.assignedAt),
    };
  }
}
