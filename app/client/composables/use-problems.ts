import type { DepartmentId } from "~/repositories/department";
import type { MunicipalityId } from "~/repositories/municipality";
import {
  createProblemRepository,
  type GetProblemsParams,
  type ProblemId,
} from "~/repositories/problem";

export const useProblems = () => {
  const { $api } = useNuxtApp();
  const problemRepository = createProblemRepository($api);

  function getProblems() {
    return useAsyncData("problems", () => problemRepository.findAll());
  }

  function getProblemTypes() {
    return useAsyncData("problems-types", () =>
      problemRepository.findAllTypes()
    );
  }

  function getProblemsByDepartmentId(
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    params?: GetProblemsParams
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-problems`,
      () =>
        problemRepository.findAllVisibleByDepartmentId(
          municipalityId,
          departmentId,
          params
        )
    );
  }

  function getProblemInDepartmentContext(
    municipalityId: MunicipalityId,
    departmentId: DepartmentId,
    problemId: ProblemId
  ) {
    return useAsyncData(
      `municipalities-${municipalityId}-departments-${departmentId}-problems-${problemId}`,
      () =>
        problemRepository.findByIdInDepartmentContext(
          municipalityId,
          departmentId,
          problemId
        )
    );
  }

  return {
    getProblems,
    getProblemTypes,
    getProblemsByDepartmentId,
    getProblemInDepartmentContext,
  };
};
