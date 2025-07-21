export type Api = typeof $fetch;

export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

type SortDirection = "asc" | "desc";

interface SortField {
  field: string;
  direction: SortDirection;
}

export interface Pageable {
  page?: number;
  size?: number;
  sort?: SortField[];
}

export function toParams(pageable: Pageable): string {
  const params = new URLSearchParams();

  if (pageable.page !== undefined) {
    params.append("page", pageable.page.toString());
  }

  if (pageable.size !== undefined) {
    params.append("size", pageable.size.toString());
  }

  if (pageable.sort) {
    for (const sortField of pageable.sort) {
      params.append("sort", [sortField.field, sortField.direction].join(","));
    }
  }

  return params.toString();
}
