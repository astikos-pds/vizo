import type { AffiliationRequest, UserProfile } from "~/types/domain";

export const getProfileUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<UserProfile>("/users/me", {
    method: "GET",
  });
};

export const getUserAffiliationsUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<AffiliationRequest[]>("/officials/me/affiliations", {
    method: "GET",
  });
};
