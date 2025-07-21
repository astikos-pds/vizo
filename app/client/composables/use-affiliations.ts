import {
  createAffiliationRepository,
  type AffiliationId,
  type GetAllAffiliationsByMunicipalityIdParams,
  type MunicipalityId,
  type UpdateAffiliationStatusRequest,
} from "~/repositories/affiliation";

export const useAffiliations = () => {
  const { $api } = useNuxtApp();
  const affiliationRepository = createAffiliationRepository($api);

  function getAffiliationsByMunicipalityId(
    municipalityId: MunicipalityId,
    params: GetAllAffiliationsByMunicipalityIdParams
  ) {
    return useAsyncData(`municipalities-${municipalityId}-affiliations`, () =>
      affiliationRepository.findAllByMunicipalityId(municipalityId, params)
    );
  }

  function updateAffiliationStatus(
    municipalityId: MunicipalityId,
    affiliationId: AffiliationId,
    body: UpdateAffiliationStatusRequest
  ) {
    return affiliationRepository.updateStatus(
      municipalityId,
      affiliationId,
      body
    );
  }

  return {
    getAffiliationsByMunicipalityId,
    updateAffiliationStatus,
  };
};
