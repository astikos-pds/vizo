import type { UseFetchOptions } from "#app";
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Assignment,
  Department,
  Municipality,
  ProblemType,
} from "~/types/domain";
import type { Page, Pageable } from "~/types/http";

type GetMunicipalityByIdResponse = Municipality;

type GetAllAffiliationsResponse = Page<AffiliationRequest>;

type UpdateAffiliationStatusRequest = {
  status: AffiliationRequestStatus;
};
type UpdateAffiliationStatusResponse = AffiliationRequest;

type GetAllDepartmentsResponse = Page<Department>;

type CreateDepartmentRequest = {
  name: string;
  iconUrl: string;
  colorHex: string;
  problemTypes: ProblemType[];
};
type CreateDepartmentResponse = Department;

type GetAllAssignmentsOfDepartmentResponse = Page<Assignment>;

type AssignToDepartmentInBatchRequest = {
  ids: string[];
};
type AssignToDepartmentInBatchResponse = Assignment[];

export const municipalityRepository = {
  getById: (id: string, options?: UseFetchOptions<Municipality>) => {
    return useQuery<GetMunicipalityByIdResponse>(
      `/municipalities/${id}`,
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

  updateAffiliationStatus: (
    municipalityId: String,
    affiliationRequestId: string,
    body: UpdateAffiliationStatusRequest
  ) => {
    const { $api } = useNuxtApp();
    return $api<UpdateAffiliationStatusResponse>(
      `/municipalities/${municipalityId}/affiliations/${affiliationRequestId}`,
      {
        method: "PATCH",
        body,
      }
    );
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

  createDepartment: (municipalityId: string, body: CreateDepartmentRequest) => {
    const { $api } = useNuxtApp();
    return $api<CreateDepartmentResponse>(
      `/municipalities/${municipalityId}/departments`,
      {
        method: "POST",
        body,
      }
    );
  },

  deleteDepartment: (municipalityId: string, departmentId: string) => {
    const { $api } = useNuxtApp();
    return $api(
      `/municipalities/${municipalityId}/departments/${departmentId}`,
      {
        method: "DELETE",
      }
    );
  },

  getAllAssignmentsOfDepartment: (
    municipalityId: string,
    departmentId: string,
    params?: Pageable,
    options?: UseFetchOptions<GetAllAssignmentsOfDepartmentResponse>
  ) => {
    return useQuery(
      `/municipalities/${municipalityId}/departments/${departmentId}/assignments`,
      {
        query: {
          ...params,
        },
        ...options,
      }
    );
  },

  assignToDepartmentInBatch: (
    municipalityId: string,
    departmentId: string,
    body: AssignToDepartmentInBatchRequest
  ) => {
    const { $api } = useNuxtApp();
    return $api<AssignToDepartmentInBatchResponse>(
      `/municipalities/${municipalityId}/departments/${departmentId}/assignments/batch`,
      {
        method: "PUT",
        body,
      }
    );
  },
};
