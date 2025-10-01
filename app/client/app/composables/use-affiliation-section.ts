import type { AffiliationFilter } from "~/services/affiliated-user";
import type { Pagination } from "~/types/domain/pagination";

export const useAffiliationSection = async (
  municipalityId: string,
  filter?: AffiliationFilter
) => {
  const { getUsersAffiliatedToMunicipality } = useAffiliatedUsers();

  const pagination = reactive<Pagination>({
    page: 0,
    size: 15,
  });

  const { data, pending } = await getUsersAffiliatedToMunicipality(
    municipalityId,
    { ...pagination, ...filter }
  );

  const { items, currentPage, totalElements } = usePagination(pagination, data);

  return {
    data,
    pending,
    items,
    currentPage,
    totalElements,
    pagination,
  };
};
