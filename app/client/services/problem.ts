import type { Problem } from "~/types/domain";

export const getProblemsUseCase = async () => {
  const { ensureAuthenticated } = useAuth();
  const ok = await ensureAuthenticated();
  if (!ok) {
    await navigateTo("/login");
  }

  const app = useNuxtApp();
  const accessToken = useCookie("access_token");

  return await app.$api<Problem[]>("/problems", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken.value}`,
    },
  });
};
