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
import type {
  NotificationDTO,
  NotificationMapper,
} from "~/types/domain/notification/notification";
import type {
  Platform,
  PushTokenDTO,
  PushTokenMapper,
} from "~/types/domain/push";

export interface ReportFilter {
  latitude?: number;
  longitude?: number;
  radius?: number;
}

export type NotificationParams = { read?: boolean } | Pagination;

export interface RegisterPushTokenRequest {
  token: string;
  platform: Platform;
}

export class MeService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pageMapper: PageMapper,
    private readonly userMapper: UserMapper,
    private readonly notificationMapper: NotificationMapper,
    private readonly pointOfInterestMapper: PointOfInterestMapper,
    private readonly reportMapper: ReportMapper,
    private readonly affiliatedUserMapper: AffiliatedUserMapper,
    private readonly assignedUserMapper: AssignedUserMapper,
    private readonly pushTokenMapper: PushTokenMapper
  ) {}

  public async getMe() {
    const response = await this.httpClient.get<UserDTO>("/me");

    return this.userMapper.toModel(response);
  }

  public async getMyNotifications(params: NotificationParams) {
    const response = await this.httpClient.get<PageDTO<NotificationDTO>>(
      "/me/notifications",
      params
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.notificationMapper.toModel(t));
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

  public async getMyAffiliations(
    params?: Pagination & AffiliationFilter,
    header?: { Authorization: string }
  ) {
    const response = await this.httpClient.get<PageDTO<AffiliatedUserDTO>>(
      "/me/affiliations",
      params,
      header
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

  public async registerPushToken(request: RegisterPushTokenRequest) {
    const response = await this.httpClient.put<PushTokenDTO>(
      "/me/push-tokens",
      request
    );

    return this.pushTokenMapper.toModel(response);
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
