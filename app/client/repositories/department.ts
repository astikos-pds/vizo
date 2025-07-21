import type { Api, Page, Pageable } from "~/types/http";
import type { MunicipalityId } from "./affiliation";
import type { Department, ProblemType } from "~/types/domain";

export type DepartmentId = Department["id"];

export type GetAllDepartmentsParams = Pageable;
export type GetAllDepartmentsResponse = Page<Department>;

export interface CreateDepartmentRequest {
  name: string;
  iconUrl: string;
  colorHex: string;
  problemTypes: ProblemType[];
}
export type CreateDepartmentResponse = Department;

export const createDepartmentRepository = (api: Api) => ({
  findAllByMunicipalityId: (
    municipalityId: MunicipalityId,
    params: GetAllDepartmentsParams
  ) => {
    return api<GetAllDepartmentsResponse>(
      `/municipalities/${municipalityId}/departments`,
      {
        method: "GET",
        params,
      }
    );
  },

  create: (municipalityId: MunicipalityId, body: CreateDepartmentRequest) => {
    return api<CreateDepartmentResponse>(
      `/municipalities/${municipalityId}/departments`,
      {
        method: "POST",
        body,
      }
    );
  },

  delete: (municipalityId: MunicipalityId, departmentId: DepartmentId) => {
    return api(
      `/municipalities/${municipalityId}/departments/${departmentId}`,
      {
        method: "DELETE",
      }
    );
  },
});
