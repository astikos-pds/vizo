import type { Municipality } from "~/types/domain";
import type { Api } from "~/types/http";

export type MunicipalityId = Municipality["id"];

export type GetMunicipalityResponse = Municipality;

export type GetMunicipalityByDomainParams = {
  domain: string;
};

export const createMunicipalityRepository = (api: Api) => ({
  findById: (id: MunicipalityId) => {
    return api<GetMunicipalityResponse>(`/municipalities/${id}`);
  },

  findByDomain: (params: GetMunicipalityByDomainParams) => {
    return api<GetMunicipalityResponse>("/municipalities", {
      method: "GET",
      params,
    });
  },
});
