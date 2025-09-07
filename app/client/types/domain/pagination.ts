export interface Pagination {
  page: number;
  size?: number;
}

export interface PageDTO<T> {
  content: T[];
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

export class Page<T> {
  constructor(
    public readonly content: T[],
    public readonly page: number,
    public readonly size: number,
    public readonly totalPages: number,
    public readonly totalElements: number
  ) {}

  public map<R>(mapper: (t: T) => R): Page<R> {
    return new Page<R>(
      this.content.map(mapper),
      this.page,
      this.size,
      this.totalPages,
      this.totalElements
    );
  }
}

export class PageMapper {
  public toModel<T>(dto: PageDTO<T>): Page<T> {
    return new Page(
      dto.content,
      dto.page,
      dto.size,
      dto.totalPages,
      dto.totalElements
    );
  }
}
