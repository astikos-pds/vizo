import type { ProblemType, Report } from "~/types/domain";
import { toParams, type Page, type Pageable } from "~/types/http";

export interface ReportRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
  problemType: ProblemType;
}

export const reportUseCase = async (
  request: ReportRequest
): Promise<Report> => {
  const app = useNuxtApp();

  return await app.$api<Report>("/reports", {
    method: "POST",
    body: request,
  });
};

interface ReportFilter {
  latitude: number;
  longitude: number;
  radius: number;
}
export interface GetReportsParams {
  filter?: ReportFilter;
  pageable?: Pageable;
}
export type GetReportsResponse = Page<Report>;
export const getReportsUseCase = async (
  params: GetReportsParams
): Promise<GetReportsResponse> => {
  const app = useNuxtApp();

  const query = new URLSearchParams();

  if (params.filter) {
    query.append("latitude", params.filter.latitude.toString());
    query.append("longitude", params.filter.longitude.toString());
    query.append("radius", params.filter.radius.toString());
  }

  if (params.pageable) {
    const pageableParams = toParams(params.pageable);
    for (const [key, value] of new URLSearchParams(pageableParams)) {
      query.append(key, value);
    }
  }

  const url = `/reports?${query.toString()}`;

  return await app.$api<GetReportsResponse>(url, {
    method: "GET",
  });
};
