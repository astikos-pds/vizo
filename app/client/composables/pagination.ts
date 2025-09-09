import type { PageDTO, Pagination } from "~/types/domain/pagination";

export const usePagination = <T>(
  pagination: Pagination,
  paginated: Ref<PageDTO<T> | null>
) => {
  const items = computed<T[]>(() => {
    if (!paginated.value) return [];

    return paginated.value.content;
  });

  const currentPage = computed({
    get: () => pagination.page + 1,
    set: (val: number) => (pagination.page = val - 1),
  });

  const totalElements = computed<number>(() => {
    if (!paginated.value) return 100;

    return paginated.value.totalElements;
  });

  return {
    items,
    currentPage,
    totalElements,
  };
};
