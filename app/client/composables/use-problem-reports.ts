import { useQuery } from "~/composables/use-query";
import type { Report } from "~/types/domain";
import { toParams, type Page, type Pageable } from "~/types/http";

export const useProblemReports = (problemId: string, pageable: Pageable) => {
  const query = computed(() => {
    const params = toParams(pageable);
    return `problems/${problemId}/reports?${params}`;
  });

  const { data, pending, error, refresh } = useQuery<Page<Report>>(query);

  return {
    reports: data,
    loading: pending,
    error,
    refresh,
  };
};
