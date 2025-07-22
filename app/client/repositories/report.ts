import type { Problem, ProblemType, Report } from "~/types/domain";
import type { Api, Page, Pageable } from "~/types/http";

export interface CreateReportRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
  problemType: ProblemType;
}

export type GetReportsResponse = Page<Report>;

export type GetReportsParams = Pageable & {
  latitude: number;
  longitude: number;
  radius: number;
};

export type GetReportsByProblemIdParams = Pageable;

export const createReportRepository = (api: Api) => ({
  create: (request: CreateReportRequest) => {
    return api<Report>("/reports", {
      method: "POST",
      body: request,
    });
  },

  findAll: (params: GetReportsParams) => {
    return api<GetReportsResponse>("/reports", {
      method: "GET",
      params,
    });
  },

  findAllByProblemId: (
    problemId: Problem["id"],
    params: GetReportsByProblemIdParams
  ) => {
    return api<GetReportsResponse>(`/problems/${problemId}/reports`, {
      method: "GET",
      params,
    });
  },
});
