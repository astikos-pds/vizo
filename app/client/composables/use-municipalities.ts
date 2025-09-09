import type { Municipality } from "~/types/domain/municipality";

export const useMunicipalities = () => {
  const { $municipalityService } = useNuxtApp();

  function getMunicipalityById(id: Municipality["id"]) {
    return useAsyncData(`municipality-${id}`, () =>
      $municipalityService.getMunicipalityById(id)
    );
  }

  function getMunicipalityByEmailDomain(emailDomain: string) {
    return useAsyncData(`municipality-${emailDomain}`, () =>
      $municipalityService.getMunicipalityByEmailDomain(emailDomain)
    );
  }

  return {
    getMunicipalityById,
    getMunicipalityByEmailDomain,
  };
};
