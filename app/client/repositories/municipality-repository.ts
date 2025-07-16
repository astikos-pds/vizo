import type { UseFetchOptions } from "#app";
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Municipality,
} from "~/types/domain";
import type { Page } from "~/types/http";

type GetMunicipalityByIdResponse = Municipality;

type GetAllAffiliationsResponse = Page<AffiliationRequest>;

export const municipalityRepository = {
  getById: (id: string, options?: UseFetchOptions<Municipality>) => {
    return useQuery<GetMunicipalityByIdResponse>(
      `/municipalitites/${id}`,
      options
    );
  },

  getAllAffiliations: (
    municipalityId: string,
    options?: UseFetchOptions<GetAllAffiliationsResponse>
  ) => {
    return useQuery(`/municipalities/${municipalityId}/affiliations`, options);
  },

  getAllAffiliationsByStatus: (
    municipalityId: string,
    status: AffiliationRequestStatus,
    options?: UseFetchOptions<GetAllAffiliationsResponse>
  ) => {
    return useQuery(`/municipalities/${municipalityId}/affiliations`, {
      query: {
        status,
      },
      ...options,
    });
  },
};
