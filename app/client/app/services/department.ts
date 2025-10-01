import type {
  DepartmentDTO,
  DepartmentMapper,
} from "~/types/domain/department";
import type {
  PageDTO,
  PageMapper,
  Pagination,
} from "~/types/domain/pagination";
import type {
  ProblemDTO,
  ProblemMapper,
  ProblemStatus,
  ProblemType,
} from "~/types/domain/problem";

import type { HttpClient } from "~/utils/http";

export interface MutateDepartmentRequest {
  name: string;
  iconUrl?: string;
  colorHex: string;
  problemTypes: ProblemType[];
}

export interface ChangeProblemStatusInScopeRequest {
  status: ProblemStatus;
}

export interface GetProblemsInScopeStatisticsParams {
  start: Date;
  end: Date;
  statuses?: ProblemStatus[];
  types?: ProblemType[];
}

export interface ProblemStatistics {
  date: string;
  count: number;
}

export interface ProblemCountByStatus {
  status: ProblemStatus;
  count: number;
}

export class DepartmentService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pageMapper: PageMapper,
    private readonly departmentMapper: DepartmentMapper,
    private readonly problemMapper: ProblemMapper
  ) {}

  public async getDepartmentById(municipalityId: string, departmentId: string) {
    const response = await this.httpClient.get<DepartmentDTO>(
      `municipalities/${municipalityId}/departments/${departmentId}`
    );

    return this.departmentMapper.toModel(response);
  }

  public async createDepartment(
    municipalityId: string,
    request: MutateDepartmentRequest
  ) {
    const response = await this.httpClient.post<DepartmentDTO>(
      `municipalities/${municipalityId}/departments`,
      request
    );

    return this.departmentMapper.toModel(response);
  }

  public async updateDepartment(
    municipalityId: string,
    departmentId: string,
    request: MutateDepartmentRequest
  ) {
    const response = await this.httpClient.put<DepartmentDTO>(
      `municipalities/${municipalityId}/departments/${departmentId}`,
      request
    );

    return this.departmentMapper.toModel(response);
  }

  public async deleteDepartment(municipalityId: string, departmentId: string) {
    this.httpClient.delete(
      `municipalities/${municipalityId}/departments/${departmentId}`
    );
  }

  public async getProblemsInScope(
    municipalityId: string,
    departmentId: string,
    pagination: Pagination
  ) {
    const response = await this.httpClient.get<PageDTO<ProblemDTO>>(
      `municipalities/${municipalityId}/departments/${departmentId}/problems`,
      pagination
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.problemMapper.toModel(t));
  }

  public async getProblemInScope(
    municipalityId: string,
    departmentId: string,
    problemId: string
  ) {
    const response = await this.httpClient.get<ProblemDTO>(
      `municipalities/${municipalityId}/departments/${departmentId}/problems/${problemId}`
    );

    return this.problemMapper.toModel(response);
  }

  public async changeProblemStatusInScope(
    municipalityId: string,
    departmentId: string,
    problemId: string,
    request: ChangeProblemStatusInScopeRequest
  ) {
    const response = await this.httpClient.patch<ProblemDTO>(
      `municipalities/${municipalityId}/departments/${departmentId}/problems/${problemId}`,
      request
    );

    return this.problemMapper.toModel(response);
  }

  public async getProblemsInScopeStatistics(
    municipalityId: string,
    departmentId: string,
    params: GetProblemsInScopeStatisticsParams
  ) {
    const response = await this.httpClient.get<ProblemStatistics[]>(
      `municipalities/${municipalityId}/departments/${departmentId}/problems/statistics`,
      {
        start: params.start.toISOString().split("T")[0],
        end: params.end.toISOString().split("T")[0],
        statuses: params.statuses,
        types: params.types,
      }
    );

    return response.map((a) => {
      return {
        date: new Date(a.date),
        count: a.count,
      };
    });
  }

  public countProblemsInScopeByStatus(
    municipalityId: string,
    departmentId: string
  ) {
    return this.httpClient.get<ProblemCountByStatus[]>(
      `municipalities/${municipalityId}/departments/${departmentId}/problems/count`
    );
  }
}
