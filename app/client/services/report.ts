import type { Report } from "~/types/domain";

export interface ReportRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
}

export const reportUseCase = async (
  request: ReportRequest
): Promise<Report> => {
  const app = useNuxtApp();

  return await app.$api<Report>("/reports", {
    method: "POST",
    body: { ...request },
  });
};
