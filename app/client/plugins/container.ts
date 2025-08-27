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
import { PermissionPresetMapper } from "~/types/domain/permission";
import { PointOfInterestMapper } from "~/types/domain/point-of-interest";
import { ProblemMapper } from "~/types/domain/problem";
import { ReportMapper } from "~/types/domain/report";
import { UserMapper } from "~/types/domain/user";
import { HttpClientImpl } from "~/utils/http";

export default defineNuxtPlugin((nuxtApp) => {
  const api = nuxtApp.vueApp.$nuxt.$api;
  const httpClient = new HttpClientImpl(api);

  const userMapper = new UserMapper();
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

  const services = {
    authService: new AuthService(httpClient, userMapper),
    meService: new MeService(
      httpClient,
      pointOfInterestMapper,
      reportMapper,
      affiliatedUserMapper,
      assignedUserMapper
    ),
    reportService: new ReportService(httpClient, reportMapper),
    problemSerivce: new ProblemService(httpClient, problemMapper, reportMapper),
    pointOfInterestService: new PointOfInterestService(
      httpClient,
      pointOfInterestMapper
    ),
    municipalityService: new MunicipalityService(
      httpClient,
      municipalityMapper
    ),
    departmentService: new DepartmentService(
      httpClient,
      departmentMapper,
      problemMapper
    ),
    affiliatedUserService: new AffiliatedUserService(
      httpClient,
      affiliatedUserMapper
    ),
    assignedUserService: new AssignedUserService(
      httpClient,
      assignedUserMapper
    ),
    permissionPresetService: new PermissionPresetService(
      httpClient,
      permissionPresetMapper
    ),
  };

  return {
    provide: {
      services,
    },
  };
});
