import type {
  AssignedUserDTO,
  AssignedUserMapper,
} from "~/types/domain/assigned-user";
import type {
  PageDTO,
  PageMapper,
  Pagination,
} from "~/types/domain/pagination";
import type { Permission, PermissionMode } from "~/types/domain/permission";
import type { HttpClient } from "~/utils/http";

export interface AssignUserToDepartmentRequest {
  affiliationId: string;
}

export interface AssignUsersToDepartmentRequest {
  affiliationsIds: string[];
}

export interface ChangeAssigneePermissionRequest {
  permissionMode: PermissionMode;
  customPermission: Permission;
  permissionPresetId: string;
}

export class AssignedUserService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pageMapper: PageMapper,
    private readonly assignedUserMapper: AssignedUserMapper
  ) {}

  public async getUsersAssignedToDepartment(
    municipalityId: string,
    departmentId: string,
    pagination?: Pagination
  ) {
    const response = await this.httpClient.get<PageDTO<AssignedUserDTO>>(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments`,
      pagination
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.assignedUserMapper.toModel(t));
  }

  public async getUserAssignedToDepartment(
    municipalityId: string,
    departmentId: string,
    assignmentId: string
  ) {
    const response = await this.httpClient.get<AssignedUserDTO>(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments/${assignmentId}`
    );

    return this.assignedUserMapper.toModel(response);
  }

  public async assignUserToDepartment(
    municipalityId: string,
    departmentId: string,
    request: AssignUserToDepartmentRequest
  ) {
    const response = await this.httpClient.put<AssignedUserDTO>(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments`,
      request
    );

    return this.assignedUserMapper.toModel(response);
  }

  public async assignUsersToDepartment(
    municipalityId: string,
    departmentId: string,
    request: AssignUsersToDepartmentRequest
  ) {
    const response = await this.httpClient.put<AssignedUserDTO[]>(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments`,
      request
    );

    return response.map(this.assignedUserMapper.toModel);
  }

  public async changeAssigneePermission(
    municipalityId: string,
    departmentId: string,
    assignmentId: string,
    request: ChangeAssigneePermissionRequest
  ) {
    const response = await this.httpClient.patch<AssignedUserDTO>(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments/${assignmentId}`,
      request
    );

    return this.assignedUserMapper.toModel(response);
  }

  public async removeAssigneeFromDepartment(
    municipalityId: string,
    departmentId: string,
    assignmentId: string
  ) {
    this.httpClient.delete(
      `/municipalities/${municipalityId}/department/${departmentId}/assignments/${assignmentId}`
    );
  }
}
