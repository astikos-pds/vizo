import { useQuery } from "~/composables/use-query";
import type { Report } from "~/types/domain";

export const useProblemReports = (problemId: Ref<string> | string) => {
  const { data, pending, error, refresh } = useQuery<Report[]>(
    `problems/${problemId}/reports`
  );

  return {
    reports: data,
    loading: pending,
    error,
    refresh,
  };
};
