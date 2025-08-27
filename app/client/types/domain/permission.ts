import type {
  Municipality,
  MunicipalityDTO,
  MunicipalityMapper,
} from "./municipality";

export type PermissionMode = "PRESET" | "CUSTOM";

export interface Permission {
  canViewReports: boolean;
  canUpdateStatus: boolean;
  canManageUsers: boolean;
}

export interface PermissionPresetDTO {
  id: string;
  name: string;
  municipality: MunicipalityDTO;
  permission: Permission;
}

export interface PermissionPreset {
  id: string;
  name: string;
  municipality: Municipality;
  permission: Permission;
}

export class PermissionPresetMapper {
  constructor(private readonly municipalityMapper: MunicipalityMapper) {}

  public toModel(dto: PermissionPresetDTO): PermissionPreset {
    return {
      ...dto,
      municipality: this.municipalityMapper.toModel(dto.municipality),
    };
  }
}
