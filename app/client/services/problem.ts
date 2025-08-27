import type { Page } from "~/types/domain/pagination";
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

  public async getReportsForProblem(problemId: string) {
    const response = await this.httpClient.get<Page<ReportDTO>>(
      `/problems/${problemId}/reports`
    );

    return response.map(this.reportMapper.toModel);
  }
}
