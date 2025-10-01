import type {
  PageDTO,
  PageMapper,
  Pagination,
} from "~/types/domain/pagination";
import type {
  ProblemDTO,
  ProblemMapper,
  ProblemType,
} from "~/types/domain/problem";
import type { ReportDTO, ReportMapper } from "~/types/domain/report";
import type { HttpClient } from "~/utils/http";

export class ProblemService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pageMapper: PageMapper,
    private readonly problemMapper: ProblemMapper,
    private readonly reportMapper: ReportMapper
  ) {}

  public async getProblems() {
    const response = await this.httpClient.get<ProblemDTO[]>("/problems");

    return response.map(this.problemMapper.toModel);
  }

  public async getProblemTypes() {
    return await this.httpClient.get<ProblemType[]>("/problems/types");
  }

  public async getReportsForProblem(
    problemId: string,
    pagination?: Pagination
  ) {
    const response = await this.httpClient.get<PageDTO<ReportDTO>>(
      `/problems/${problemId}/reports`,
      pagination
    );

    const page = this.pageMapper.toModel(response);

    return page.map((t) => this.reportMapper.toModel(t));
  }
}
