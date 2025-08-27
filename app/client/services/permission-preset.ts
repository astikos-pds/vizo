import type {
  Permission,
  PermissionPresetDTO,
  PermissionPresetMapper,
} from "~/types/domain/permission";
import type { HttpClient } from "~/utils/http";

export interface MutatePermissionPresetRequest {
  name: string;
  permission: Permission;
}

export class PermissionPresetService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly permissionPresetMapper: PermissionPresetMapper
  ) {}

  public async getPermissionPresetsInMunicipality(municipalityId: string) {
    const response = await this.httpClient.get<PermissionPresetDTO[]>(
      `/municipalities/${municipalityId}/permission-presets`
    );

    return response.map(this.permissionPresetMapper.toModel);
  }

  public async getPermissionPresetInMunicipality(municipalityId: string) {
    const response = await this.httpClient.get<PermissionPresetDTO>(
      `/municipalities/${municipalityId}/permission-presets`
    );

    return this.permissionPresetMapper.toModel(response);
  }

  public async createPermissionPreset(
    municipalityId: string,
    request: MutatePermissionPresetRequest
  ) {
    const response = await this.httpClient.post<PermissionPresetDTO>(
      `/municipalities/${municipalityId}/permission-presets`,
      request
    );

    return this.permissionPresetMapper.toModel(response);
  }

  public async updatePermissionPreset(
    municipalityId: string,
    permissionPresetId: string,
    request: MutatePermissionPresetRequest
  ) {
    const response = await this.httpClient.put<PermissionPresetDTO>(
      `/municipalities/${municipalityId}/permission-presets/${permissionPresetId}`,
      request
    );

    return this.permissionPresetMapper.toModel(response);
  }

  public async deletePermissionPreset(
    municipalityId: string,
    permissionPresetId: string
  ) {
    this.httpClient.delete(
      `/municipalities/${municipalityId}/permission-presets/${permissionPresetId}`
    );
  }
}
