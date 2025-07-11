import type { Municipality } from "~/types/domain";

export interface GetMunicipalityByDomainFilter {
  domain: string;
}

export const getMunicipalityByDomainUseCase = async (
  filter: GetMunicipalityByDomainFilter
) => {
  const app = useNuxtApp();

  const query = new URLSearchParams();
  query.append("domain", filter.domain);

  const url = `/municipalities?${query.toString()}`;

  return await app.$api<Municipality>(url, {
    method: "GET",
  });
};
