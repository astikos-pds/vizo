import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Assignment,
  Municipality,
  UserProfile,
} from "~/types/domain";
import type { Api } from "~/types/http";

export type GetAllAffiliationsResponse = AffiliationRequest[];

export type GetAllAssignmentsResponse = Assignment[];

export const createUserRepository = (api: Api) => ({
  getProfile: () => {
    return api<UserProfile>("/users/me", { method: "GET" });
  },

  getAllAffiliations: () => {
    return api<GetAllAffiliationsResponse>("/officials/me/affiliations", {
      method: "GET",
    });
  },

  getAllAssignmentsByMunicipalityId: (municipalityId: Municipality["id"]) => {
    return api<GetAllAssignmentsResponse>(
      `/officials/me/municipalities/${municipalityId}/assignments`,
      {
        method: "GET",
      }
    );
  },
});
