import {
  createMunicipalityRepository,
  type GetMunicipalityByDomainParams,
  type MunicipalityId,
} from "~/repositories/municipality";

export const useMunicipalities = () => {
  const { $api } = useNuxtApp();
  const municipalityRepository = createMunicipalityRepository($api);

  function getMunicipalityById(id: MunicipalityId) {
    return useAsyncData(`municipality-${id}`, () =>
      municipalityRepository.findById(id)
    );
  }

  function getMunicipalityByDomain(params: GetMunicipalityByDomainParams) {
    return municipalityRepository.findByDomain(params);
  }

  return {
    getMunicipalityById,
    getMunicipalityByDomain,
  };
};
