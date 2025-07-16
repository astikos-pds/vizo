import type { UseFetchOptions } from "#app";
import type { Municipality } from "~/types/domain";

type GetMunicipalityByIdResponse = Municipality;

export const municipalityRepository = {
  getById: (id: string, options?: UseFetchOptions<Municipality>) => {
    return useQuery<GetMunicipalityByIdResponse>(
      `/municipalitites/${id}`,
      options
    );
  },
};
