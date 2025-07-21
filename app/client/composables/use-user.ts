import { createUserRepository } from "~/repositories/user";
import type { Municipality } from "~/types/domain";

export const useUser = () => {
  const { $api } = useNuxtApp();
  const userRepository = createUserRepository($api);

  function getProfile() {
    return useAsyncData("profile", () => userRepository.getProfile());
  }

  function getAffiliations() {
    return useAsyncData("affiliations", () =>
      userRepository.getAllAffiliations()
    );
  }

  function getAssignmentsByMunicipalityId(municipalityId: Municipality["id"]) {
    return useAsyncData(`municipalities-${municipalityId}-assignments`, () =>
      userRepository.getAllAssignmentsByMunicipalityId(municipalityId)
    );
  }

  return {
    getProfile,
    getAffiliations,
    getAssignmentsByMunicipalityId,
  };
};
