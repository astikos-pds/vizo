import type { UseFetchOptions } from "#app";
import type { ProblemType, Report } from "~/types/domain";
import type { Page } from "~/types/http";

type GetReportsByIdResponse = Page<Report>;

type GetAllProblemTypesResponse = ProblemType[];

export const problemRepository = {
  getReportsById: (
    problemId: string,
    options?: UseFetchOptions<GetReportsByIdResponse>
  ) => {
    return useQuery(`/problems/${problemId}/reports`, options);
  },

  getAllProblemTypes: (
    options?: UseFetchOptions<GetAllProblemTypesResponse>
  ) => {
    return useQuery("/problems/types", options);
  },
};
