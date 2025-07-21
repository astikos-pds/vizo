import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Municipality,
} from "~/types/domain";
import type { Api, Page, Pageable } from "~/types/http";

export type MunicipalityId = Municipality["id"];
export type AffiliationId = AffiliationRequest["id"];

export type GetAllAffiliationsByMunicipalityIdParams = Pageable & {
  status?: AffiliationRequestStatus;
};
export type GetAllAffiliationsByMunicipalityIdResponse =
  Page<AffiliationRequest>;

export type UpdateAffiliationStatusRequest = {
  status: AffiliationRequestStatus;
};
export type UpdateAffiliationStatusResponse = AffiliationRequest;

export const createAffiliationRepository = (api: Api) => ({
  findAllByMunicipalityId: (
    municipalityId: MunicipalityId,
    params: GetAllAffiliationsByMunicipalityIdParams
  ) => {
    return api<GetAllAffiliationsByMunicipalityIdResponse>(
      `/municipalities/${municipalityId}/affiliations`,
      {
        method: "GET",
        params,
      }
    );
  },

  updateStatus: (
    municipalityId: MunicipalityId,
    affiliationId: AffiliationId,
    body: UpdateAffiliationStatusRequest
  ) => {
    return api<UpdateAffiliationStatusResponse>(
      `/municipalities/${municipalityId}/affiliations/${affiliationId}`,
      {
        method: "PATCH",
        body,
      }
    );
  },
});
