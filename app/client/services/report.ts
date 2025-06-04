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
  const { ensureAuthenticated } = useAuth();
  const ok = await ensureAuthenticated();
  if (!ok) {
    await navigateTo("/login");
  }

  const app = useNuxtApp();
  const accessToken = useCookie("access_token");

  return await app.$api<Report>("/reports", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken.value}`,
    },
    body: { ...request },
  });
};
