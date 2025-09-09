import type { ProblemType } from "~/types/domain/problem";
import type { ReportDTO, ReportMapper } from "~/types/domain/report";
import type { HttpClient } from "~/utils/http";

export interface ReportProblemRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
  problemType: ProblemType;
}

export class ReportService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly reportMapper: ReportMapper
  ) {}

  public async reportProblem(request: ReportProblemRequest) {
    const response = await this.httpClient.post<ReportDTO>("/reports", request);

    return this.reportMapper.toModel(response);
  }
}
