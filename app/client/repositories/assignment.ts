import type { Api, Page, Pageable } from "~/types/http";
import type { DepartmentId } from "./department";
import type { Assignment } from "~/types/domain";
import type { MunicipalityId } from "./municipality";

export type AssignmentId = Assignment["id"];

export type GetAssignmentsParams = Pageable;
export type GetAssignmentsResponse = Page<Assignment>;

export type CreateAssignmentsInBatchResquest = {
  ids: AssignmentId[];
};
export type CreateAssignmentsInBatchResponse = Assignment[];

export const createAssignmentRepository = (api: Api) => ({
  findAllByDepartmentId: (
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    params?: GetAssignmentsParams
  ) => {
    return api<GetAssignmentsResponse>(
      `/municipalities/${municipalityId}/departments/${departmentId}/assignments`,
      {
        method: "GET",
        params,
      }
    );
  },

  createInBatch: (
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    body: CreateAssignmentsInBatchResquest
  ) => {
    return api<CreateAssignmentsInBatchResponse>(
      `/municipalities/${municipalityId}/departments/${departmentId}/assignments/batch`,
      {
        method: "PUT",
        body,
      }
    );
  },
});
