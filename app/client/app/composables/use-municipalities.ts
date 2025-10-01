import type { Municipality } from "~/types/domain/municipality";

export const useMunicipalities = () => {
  const { $municipalityService } = useNuxtApp();
  const { loading, handle } = useApiHandler();

  function getMunicipalityById(id: Municipality["id"]) {
    return useAsyncData(`municipality-${id}`, () =>
      $municipalityService.getMunicipalityById(id)
    );
  }

  function getMunicipalityByEmailDomain(emailDomain: string) {
    return handle(() =>
      $municipalityService.getMunicipalityByEmailDomain(emailDomain)
    );
  }

  return {
    loading,
    getMunicipalityById,
    getMunicipalityByEmailDomain,
  };
};
