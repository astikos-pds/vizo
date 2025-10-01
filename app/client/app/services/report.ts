import type { Report, ReportDTO, ReportMapper } from "~/types/domain/report";
import type { HttpClient } from "~/utils/http";

export interface ReportProblemRequest {
  description: string;
  imagesUrls: string[];
  latitude: number;
  longitude: number;
}

export class ReportService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly reportMapper: ReportMapper
  ) {}

  public async getReport(id: Report["id"]) {
    const response = await this.httpClient.get<ReportDTO>(`/reports/${id}`);

    return this.reportMapper.toModel(response);
  }

  public async reportProblem(request: ReportProblemRequest) {
    const response = await this.httpClient.post<ReportDTO>("/reports", request);

    return this.reportMapper.toModel(response);
  }

  public async updateReport(id: Report["id"], request: ReportProblemRequest) {
    const response = await this.httpClient.put<ReportDTO>(
      `/reports/${id}`,
      request
    );

    return this.reportMapper.toModel(response);
  }

  public async deleteReport(id: Report["id"]) {
    this.httpClient.delete(`/reports/${id}`);
  }
}
