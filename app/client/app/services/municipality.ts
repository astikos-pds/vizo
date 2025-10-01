import type {
  MunicipalityDTO,
  MunicipalityMapper,
} from "~/types/domain/municipality";
import type { HttpClient } from "~/utils/http";

export class MunicipalityService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly municipalityMapper: MunicipalityMapper
  ) {}

  public async getMunicipalityById(id: string) {
    const response = await this.httpClient.get<MunicipalityDTO>(
      `/municipalities/${id}`
    );

    return this.municipalityMapper.toModel(response);
  }

  public async getMunicipalityByEmailDomain(emailDomain: string) {
    const response = await this.httpClient.get<MunicipalityDTO>(
      "/municipalities",
      {
        domain: emailDomain,
      }
    );

    return this.municipalityMapper.toModel(response);
  }
}
