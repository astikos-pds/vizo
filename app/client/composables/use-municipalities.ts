import {
  getMunicipalityByDomainUseCase,
  type GetMunicipalityByDomainFilter,
} from "~/services/municipality";
import type { Municipality } from "~/types/domain";

export const useMunicipalities = () => {
  const { loading, handle } = useApiHandler();

  async function getMunicipalityByDomain(
    filter: GetMunicipalityByDomainFilter
  ) {
    return await handle<Municipality>(() =>
      getMunicipalityByDomainUseCase(filter)
    );
  }

  return {
    loading,
    getMunicipalityByDomain,
  };
};
