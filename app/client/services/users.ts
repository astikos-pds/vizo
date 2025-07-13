import type {
  AffiliationRequest,
  Municipality,
  UserProfile,
} from "~/types/domain";

export const getProfileUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<UserProfile>("/users/me", {
    method: "GET",
  });
};

export interface MunicipalityAffiliation {
  affiliationRequest: AffiliationRequest;
  municipality: Municipality;
}

export const getMunicipalitiesAffiliationsUseCase = async () => {
  const app = useNuxtApp();

  return await app.$api<MunicipalityAffiliation[]>(
    "/officials/me/municipalities",
    {
      method: "GET",
    }
  );
};
