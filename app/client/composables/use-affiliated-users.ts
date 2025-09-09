import type {
  AffiliateToMunicipalityRequest,
  AffiliationFilter,
  ChangeAffiliationStatusRequest,
} from "~/services/affiliated-user";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Municipality } from "~/types/domain/municipality";
import type { Pagination } from "~/types/domain/pagination";

export const useAffiliatedUsers = () => {
  const { $affiliatedUserService } = useNuxtApp();
  const { handle, loading } = useApiHandler();

  function getUsersAffiliatedToMunicipality(
    municipalityId: Municipality["id"],
    params?: Pagination & AffiliationFilter
  ) {
    return useAsyncData(`municipalities-${municipalityId}-affiliations`, () =>
      $affiliatedUserService.getUsersAffiliatedToMunicipality(
        municipalityId,
        params
      )
    );
  }

  function requestAffiliationToMunicipality(
    municipalityId: Municipality["id"],
    request: AffiliateToMunicipalityRequest
  ) {
    return handle(() =>
      $affiliatedUserService.requestAffiliationToMunicipality(
        municipalityId,
        request
      )
    );
  }

  function changeAffiliationStatus(
    municipalityId: Municipality["id"],
    affiliationId: AffiliatedUser["id"],
    request: ChangeAffiliationStatusRequest
  ) {
    return handle(() =>
      $affiliatedUserService.changeAffiliationStatus(
        municipalityId,
        affiliationId,
        request
      )
    );
  }

  function removeAffiliateFromMunicipality(
    municipalityId: Municipality["id"],
    affiliationId: AffiliatedUser["id"]
  ) {
    return handle(() =>
      $affiliatedUserService.removeAffiliateFromMunicipality(
        municipalityId,
        affiliationId
      )
    );
  }

  return {
    loading,
    getUsersAffiliatedToMunicipality,
    requestAffiliationToMunicipality,
    changeAffiliationStatus,
    removeAffiliateFromMunicipality,
  };
};
