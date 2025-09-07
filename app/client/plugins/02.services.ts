import { AffiliatedUserService } from "~/services/affiliated-user";
import { AssignedUserService } from "~/services/assigned-user";
import { AuthService } from "~/services/auth";
import { DepartmentService } from "~/services/department";
import { MeService } from "~/services/me";
import { MunicipalityService } from "~/services/municipality";
import { PermissionPresetService } from "~/services/permission-preset";
import { PointOfInterestService } from "~/services/point-of-interest";
import { ProblemService } from "~/services/problem";
import { ReportService } from "~/services/report";
import { AffiliatedUserMapper } from "~/types/domain/affiliated-user";
import { AssignedUserMapper } from "~/types/domain/assigned-user";
import { DepartmentMapper } from "~/types/domain/department";
import { MunicipalityMapper } from "~/types/domain/municipality";
import { PageMapper } from "~/types/domain/pagination";
import { PermissionPresetMapper } from "~/types/domain/permission";
import { PointOfInterestMapper } from "~/types/domain/point-of-interest";
import { ProblemMapper } from "~/types/domain/problem";
import { ReportMapper } from "~/types/domain/report";
import {
  AuthenticationMapper,
  EmailVerificationMapper,
  UserMapper,
} from "~/types/domain/user";

export default defineNuxtPlugin((nuxtApp) => {
  const api = nuxtApp.vueApp.$nuxt.$api;
  const httpClient = new HttpClientImpl(api);

  const pageMapper = new PageMapper();

  const userMapper = new UserMapper();
  const emailVerificationMapper = new EmailVerificationMapper();
  const authenticationMapper = new AuthenticationMapper(userMapper);
  const pointOfInterestMapper = new PointOfInterestMapper(userMapper);
  const problemMapper = new ProblemMapper();
  const reportMapper = new ReportMapper(userMapper, problemMapper);
  const municipalityMapper = new MunicipalityMapper();
  const affiliatedUserMapper = new AffiliatedUserMapper(
    userMapper,
    municipalityMapper
  );
  const departmentMapper = new DepartmentMapper(
    municipalityMapper,
    affiliatedUserMapper
  );
  const permissionPresetMapper = new PermissionPresetMapper(municipalityMapper);
  const assignedUserMapper = new AssignedUserMapper(
    affiliatedUserMapper,
    departmentMapper,
    permissionPresetMapper
  );

  const authService: AuthService = new AuthService(
    httpClient,
    userMapper,
    authenticationMapper,
    emailVerificationMapper
  );
  const meService: MeService = new MeService(
    httpClient,
    pageMapper,
    userMapper,
    pointOfInterestMapper,
    reportMapper,
    affiliatedUserMapper,
    assignedUserMapper
  );
  const reportService: ReportService = new ReportService(
    httpClient,
    reportMapper
  );
  const problemService: ProblemService = new ProblemService(
    httpClient,
    pageMapper,
    problemMapper,
    reportMapper
  );
  const pointOfInterestService: PointOfInterestService =
    new PointOfInterestService(httpClient, pointOfInterestMapper);
  const municipalityService: MunicipalityService = new MunicipalityService(
    httpClient,
    municipalityMapper
  );
  const departmentService: DepartmentService = new DepartmentService(
    httpClient,
    pageMapper,
    departmentMapper,
    problemMapper
  );
  const affiliatedUserService: AffiliatedUserService =
    new AffiliatedUserService(httpClient, pageMapper, affiliatedUserMapper);
  const assignedUserService: AssignedUserService = new AssignedUserService(
    httpClient,
    pageMapper,
    assignedUserMapper
  );
  const permissionPresetService: PermissionPresetService =
    new PermissionPresetService(httpClient, permissionPresetMapper);

  return {
    provide: {
      authService,
      meService,
      reportService,
      problemService,
      pointOfInterestService,
      municipalityService,
      departmentService,
      affiliatedUserService,
      assignedUserService,
      permissionPresetService,
    },
  };
});
