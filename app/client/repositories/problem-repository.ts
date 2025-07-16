import type { UseFetchOptions } from "#app";
import type { ProblemType } from "~/types/domain";

type GetAllProblemTypesResponse = ProblemType[];

export const problemRepository = {
  getAllProblemTypes: (
    options?: UseFetchOptions<GetAllProblemTypesResponse>
  ) => {
    return useQuery("/problems/types", options);
  },
};
