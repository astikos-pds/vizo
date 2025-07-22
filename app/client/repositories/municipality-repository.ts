import type { UseFetchOptions } from "#app";
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Assignment,
  Department,
  Municipality,
  Problem,
  ProblemType,
} from "~/types/domain";
import type { Page, Pageable } from "~/types/http";

type GetMunicipalityByIdResponse = Municipality;

type GetAllVisibleProblemsInDepartmentResponse = Page<Problem>;

type GetDepartmentProblemByIdResponse = Problem;

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

  getAllVisibleProblemsInDepartment: (
    municipalityId: string,
    departmentId: string,
    params?: Pageable,
    options?: UseFetchOptions<GetAllVisibleProblemsInDepartmentResponse>
  ) => {
    return useQuery(
      `/municipalities/${municipalityId}/departments/${departmentId}/problems`,
      {
        query: {
          ...params,
        },
        ...options,
      }
    );
  },

  getDepartmentProblemById: (
    municipalityId: string,
    departmentId: string,
    problemId: string,
    options?: UseFetchOptions<GetDepartmentProblemByIdResponse>
  ) => {
    return useQuery(
      `/municipalities/${municipalityId}/departments/${departmentId}/problems/${problemId}`,
      options
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
