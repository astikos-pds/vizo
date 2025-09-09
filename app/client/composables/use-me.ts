import type { ReportFilter } from "~/services/me";
import type { Department } from "~/types/domain/department";
import type { Municipality } from "~/types/domain/municipality";
import type { Pagination } from "~/types/domain/pagination";

export const useMe = () => {
  const { $meService } = useNuxtApp();
  const { handle, loading } = useApiHandler();

  function getMe() {
    return useAsyncData("me", () => $meService.getMe());
  }

  function getMyReports(params?: Pagination & ReportFilter) {
    return useAsyncData("my-reports", () => $meService.getMyReports(params));
  }

  function getMyPointsOfInterest(pagination?: Pagination) {
    return useAsyncData("my-points-of-interest", () =>
      $meService.getMyPointsOfInterest(pagination)
    );
  }

  function getMyAffiliations(pagination?: Pagination) {
    return useAsyncData("my-affiliations", () =>
      $meService.getMyAffiliations(pagination)
    );
  }

  function getMyAssignmentsInMunicipality(
    municipalityId: Municipality["id"],
    pagination?: Pagination
  ) {
    return useAsyncData(`my-municipality-${municipalityId}-assignments`, () =>
      $meService.getMyAssignmentsInMunicipality(municipalityId, pagination)
    );
  }

  function disaffiliateFromMunicipality(municipalityId: Municipality["id"]) {
    return handle(() =>
      $meService.disaffiliateFromMunicipality(municipalityId)
    );
  }

  function leaveDepartment(
    municipalityId: Municipality["id"],
    departmentId: Department["id"]
  ) {
    return handle(() =>
      $meService.leaveDepartment(municipalityId, departmentId)
    );
  }

  return {
    loading,
    getMe,
    getMyReports,
    getMyPointsOfInterest,
    getMyAffiliations,
    getMyAssignmentsInMunicipality,
    disaffiliateFromMunicipality,
    leaveDepartment,
  };
};
