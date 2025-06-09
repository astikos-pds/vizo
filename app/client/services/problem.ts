import type { Problem } from "~/types/domain";

export const getProblemsUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<Problem[]>("/problems", {
    method: "GET",
  });
};
