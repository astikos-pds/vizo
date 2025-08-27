import type {
  PointOfInterestDTO,
  PointOfInterestMapper,
} from "~/types/domain/point-of-interest";
import type { HttpClient } from "~/utils/http";

export interface MutatePointOfInterestRequest {
  name: string;
  latitude: number;
  longitude: number;
  radius: number;
}

export class PointOfInterestService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly pointOfInterestMapper: PointOfInterestMapper
  ) {}

  public async getPointOfInterestById(id: string) {
    const response = await this.httpClient.get<PointOfInterestDTO>(
      `/points-of-interest/${id}`
    );

    return this.pointOfInterestMapper.toModel(response);
  }

  public async createPointOfInterest(request: MutatePointOfInterestRequest) {
    const response = await this.httpClient.post<PointOfInterestDTO>(
      "/points-of-interest",
      request
    );

    return this.pointOfInterestMapper.toModel(response);
  }

  public async updatePointOfInterest(
    id: string,
    request: MutatePointOfInterestRequest
  ) {
    const response = await this.httpClient.put<PointOfInterestDTO>(
      `/points-of-interest/${id}`,
      request
    );

    return this.pointOfInterestMapper.toModel(response);
  }

  public async deletePointOfInterest(id: string) {
    this.httpClient.delete(`/points-of-interest/${id}`);
  }
}
