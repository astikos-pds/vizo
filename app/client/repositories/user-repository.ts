import type { UseFetchOptions } from "#app";
import type { AffiliationRequest, Assignment } from "~/types/domain";

type GetAllAffiliationsResponse = AffiliationRequest[];

type GetAllAssignmentsResponse = Assignment[];

export const userRepository = {
  getAllUserAffiliations: (
    options?: UseFetchOptions<GetAllAffiliationsResponse>
  ) => {
    return useQuery("/officials/me/affiliations", options);
  },

  getAllUserAssignmentsByMunicipalityId: (
    municipalityId: string,
    options?: UseFetchOptions<GetAllAssignmentsResponse>
  ) => {
    return useQuery(
      `/officials/me/municipalities/${municipalityId}/assignments`,
      options
    );
  },
};
