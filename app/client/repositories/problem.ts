import type { Problem, ProblemType } from "~/types/domain";
import type { Api, Page, Pageable } from "~/types/http";
import type { DepartmentId } from "./department";
import type { MunicipalityId } from "./municipality";

export type ProblemId = Problem["id"];

export type GetProblemsParams = Pageable;
export type GetProblemsResponse = Page<Problem>;

export type GetProblemResponse = Problem;

export const createProblemRepository = (api: Api) => ({
  findAll: () => {
    return api<Problem[]>("/problems", {
      method: "GET",
    });
  },

  findAllTypes: () => {
    return api<ProblemType[]>("/problems/types", {
      method: "GET",
    });
  },

  findAllVisibleByDepartmentId: (
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    params?: GetProblemsParams
  ) => {
    return api<GetProblemsResponse>(
      `/municipalities/${municipalityId}/departments/${departmentId}/problems`,
      {
        method: "GET",
        params,
      }
    );
  },

  findByIdInDepartmentContext: (
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    problemId: ProblemId
  ) => {
    return api<GetProblemResponse>(
      `/municipalities/${municipalityId}/departments/${departmentId}/problems/${problemId}`,
      {
        method: "GET",
      }
    );
  },
});
