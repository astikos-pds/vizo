import type {
  AffiliatedUserDTO,
  AffiliatedUserMapper,
} from "~/types/domain/affiliated-user";
import type {
  AssignedUserDTO,
  AssignedUserMapper,
} from "~/types/domain/assigned-user";
import {
  PageMapper,
  type PageDTO,
  type Pagination,
} from "~/types/domain/pagination";
import type {
  PointOfInterestDTO,
  PointOfInterestMapper,
} from "~/types/domain/point-of-interest";
import type { ReportDTO, ReportMapper } from "~/types/domain/report";
import type { UserDTO, UserMapper } from "~/types/domain/user";
import type { HttpClient } from "~/utils/http";
import type { AffiliationFilter } from "./affiliated-user";

export type ReportFilter = {
  latitude: number;
  longitude: number;
  radius: number;
};

export class MeService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pageMapper: PageMapper,
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
    const response = await this.httpClient.get<PageDTO<PointOfInterestDTO>>(
      "/me/points-of-interest",
      pagination
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.pointOfInterestMapper.toModel(t));
  }

  public async getMyReports(params?: Pagination & ReportFilter) {
    const response = await this.httpClient.get<PageDTO<ReportDTO>>(
      "/me/reports",
      params
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.reportMapper.toModel(t));
  }

  public async getMyAffiliations(params?: Pagination & AffiliationFilter) {
    const response = await this.httpClient.get<PageDTO<AffiliatedUserDTO>>(
      "/me/affiliations",
      params
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.affiliatedUserMapper.toModel(t));
  }

  public async getMyAssignmentsInMunicipality(
    municipalityId: string,
    pagination?: Pagination
  ) {
    const response = await this.httpClient.get<PageDTO<AssignedUserDTO>>(
      `me/municipalities/${municipalityId}/assignments`,
      pagination
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.assignedUserMapper.toModel(t));
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
