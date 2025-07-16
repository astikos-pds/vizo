import type { UseFetchOptions } from "#app";
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Department,
  Municipality,
} from "~/types/domain";
import type { Page, Pageable } from "~/types/http";

type GetMunicipalityByIdResponse = Municipality;

type GetAllAffiliationsResponse = Page<AffiliationRequest>;

type GetAllDepartmentsResponse = Page<Department>;

export const municipalityRepository = {
  getById: (id: string, options?: UseFetchOptions<Municipality>) => {
    return useQuery<GetMunicipalityByIdResponse>(
      `/municipalitites/${id}`,
      options
    );
  },

  getAllAffiliations: (
    municipalityId: string,
    params?: Pageable,
    options?: UseFetchOptions<GetAllAffiliationsResponse>
  ) => {
    return useQuery(`/municipalities/${municipalityId}/affiliations`, {
      query: {
        ...params,
      },
      ...options,
    });
  },

  getAllAffiliationsByStatus: (
    municipalityId: string,
    status: AffiliationRequestStatus,
    params?: Pageable,
    options?: UseFetchOptions<GetAllAffiliationsResponse>
  ) => {
    return useQuery(`/municipalities/${municipalityId}/affiliations`, {
      query: {
        status,
        ...params,
      },
      ...options,
    });
  },

  getAllDepartments: (
    municipalityId: string,
    params?: Pageable,
    options?: UseFetchOptions<GetAllDepartmentsResponse>
  ) => {
    return useQuery(`/municipalities/${municipalityId}/departments`, {
      query: {
        ...params,
      },
      ...options,
    });
  },
};
