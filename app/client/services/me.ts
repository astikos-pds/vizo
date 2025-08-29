import type {
  AffiliatedUserDTO,
  AffiliatedUserMapper,
} from "~/types/domain/affiliated-user";
import type {
  AssignedUserDTO,
  AssignedUserMapper,
} from "~/types/domain/assigned-user";
import type { Page, Pagination } from "~/types/domain/pagination";
import type {
  PointOfInterestDTO,
  PointOfInterestMapper,
} from "~/types/domain/point-of-interest";
import type { ReportDTO, ReportMapper } from "~/types/domain/report";
import type { UserDTO, UserMapper } from "~/types/domain/user";
import type { HttpClient } from "~/utils/http";

export type ReportFilter = {
  latitude: number;
  longitude: number;
  radius: number;
};

export class MeService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly userMapper: UserMapper,
    private readonly pointOfInterestMapper: PointOfInterestMapper,
    private readonly reportMapper: ReportMapper,
    private readonly affiliatedUserMapper: AffiliatedUserMapper,
    private readonly assignedUserMapper: AssignedUserMapper
  ) {}

  public async getMe() {
    const response = await this.httpClient.get<UserDTO>("/me");

    return this.userMapper.toModel(response);
  }

  public async getMyPointsOfInterest(pagination?: Pagination) {
    const response = await this.httpClient.get<Page<PointOfInterestDTO>>(
      "/me/points-of-interest",
      pagination
    );

    return response.map(this.pointOfInterestMapper.toModel);
  }

  public async getMyReports(params?: Pagination & ReportFilter) {
    const response = await this.httpClient.get<Page<ReportDTO>>(
      "/me/reports",
      params
    );

    return response.map(this.reportMapper.toModel);
  }

  public async getMyAffiliations(pagination?: Pagination) {
    const response = await this.httpClient.get<Page<AffiliatedUserDTO>>(
      "/me/affiliations",
      pagination
    );

    return response.map(this.affiliatedUserMapper.toModel);
  }

  public async getMyAssignmentsInMunicipality(
    municipalityId: string,
    pagination?: Pagination
  ) {
    const response = await this.httpClient.get<Page<AssignedUserDTO>>(
      `me/municipalities/${municipalityId}/assignments`,
      pagination
    );

    return response.map(this.assignedUserMapper.toModel);
  }

  public async disaffiliateFromMunicipality(municipalityId: string) {
    this.httpClient.delete(`me/municipalities/${municipalityId}/affiliations`);
  }

  public async leaveDepartment(municipalityId: string, departmentId: string) {
    this.httpClient.delete(
      `me/municipalities/${municipalityId}/departments/${departmentId}`
    );
  }
}
