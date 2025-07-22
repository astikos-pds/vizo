import { createProblemRepository } from "~/repositories/problem";

export const useProblems = () => {
  const { $api } = useNuxtApp();
  const problemRepository = createProblemRepository($api);

  const { data, pending, error, refresh } = useAsyncData("problems", () =>
    problemRepository.findAll()
  );

  return {
    problems: data,
    loading: pending,
    error,
    refresh,
  };
};
