export interface ReportRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
}
export interface ReportResponse {
  id: string;
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
  citizenId: string;
  problemId: string;
  createdAt: string;
}

export const reportUseCase = async (
  request: ReportRequest
): Promise<ReportResponse> => {
  const app = useNuxtApp();
  const accessToken = useCookie("access_token");

  return await app.$api<ReportResponse>("/reports", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken.value}`,
    },
    body: { ...request },
  });
};
