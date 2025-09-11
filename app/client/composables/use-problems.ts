import type { Pagination } from "~/types/domain/pagination";
import type { Problem } from "~/types/domain/problem";

export const useProblems = () => {
  const { $problemService } = useNuxtApp();

  function getProblems() {
    return useAsyncData("problems", () => $problemService.getProblems());
  }

  function getProblemTypes() {
    return useAsyncData("problems-types", () =>
      $problemService.getProblemTypes()
    );
  }

  function getReportsForProblem(
    problemId: Problem["id"],
    pagination?: Pagination
  ) {
    return useAsyncData(`problems-${problemId}-reports`, () =>
      $problemService.getReportsForProblem(problemId, pagination)
    );
  }

  return {
    getProblems,
    getProblemTypes,
    getReportsForProblem,
  };
};
