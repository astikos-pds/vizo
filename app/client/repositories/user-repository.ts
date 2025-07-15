import type { UseFetchOptions } from "#app";
import type { AffiliationRequest } from "~/types/domain";

type GetAffiliationsResponse = AffiliationRequest[];

export const userRepository = {
  getAllUserAffiliations: (
    options?: UseFetchOptions<GetAffiliationsResponse>
  ) => {
    return useQuery<GetAffiliationsResponse>(
      "/officials/me/affiliations",
      options
    );
  },
};
