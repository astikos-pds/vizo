import type { Problem } from "~/types/domain";
import type { Api } from "~/types/http";

export const createProblemRepository = (api: Api) => ({
  findAll: () => {
    return api<Problem[]>("/problems", {
      method: "GET",
    });
  },
});
