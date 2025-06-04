import { getProblemsUseCase } from "~/services/problem";

export const useProblems = () => {
  const { data, pending, error, refresh } = useAsyncData("problems", () =>
    getProblemsUseCase()
  );

  return {
    problems: data,
    loading: pending,
    error,
    refresh,
  };
};
