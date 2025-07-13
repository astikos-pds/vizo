import type { UserProfile } from "~/types/domain";

export const getProfileUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<UserProfile>("/users/me", {
    method: "GET",
  });
};
