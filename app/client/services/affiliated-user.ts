import type {
  AffiliatedUserDTO,
  AffiliatedUserMapper,
  AffiliationStatus,
} from "~/types/domain/affiliated-user";
import type { Page, Pagination } from "~/types/domain/pagination";
import type { HttpClient } from "~/utils/http";

export interface AffiliationFilter {
  status: AffiliationStatus;
}

export interface AffiliateToMunicipalityRequest {
  institutionalEmail: string;
}

export interface ChangeAffiliationStatusRequest {
  status: AffiliationStatus;
}

export class AffiliatedUserService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly affiliatedUserMapper: AffiliatedUserMapper
  ) {}

  public async getUsersAffiliatedToMunicipality(
    municipalityId: string,
    params?: Pagination & AffiliationFilter
  ) {
    const response = await this.httpClient.get<Page<AffiliatedUserDTO>>(
      `/municipalities/${municipalityId}/affiliations`,
      params
    );

    return response.map(this.affiliatedUserMapper.toModel);
  }

  public async requestAffiliationToMunicipality(
    municipalityId: string,
    request: AffiliateToMunicipalityRequest
  ) {
    const response = await this.httpClient.post<AffiliatedUserDTO>(
      `/municipalities/${municipalityId}/affiliations`,
      request
    );

    return this.affiliatedUserMapper.toModel(response);
  }

  public async changeAffiliationStatus(
    municipalityId: string,
    affiliationId: string,
    request: ChangeAffiliationStatusRequest
  ) {
    const response = await this.httpClient.patch<AffiliatedUserDTO>(
      `/municipalities/${municipalityId}/affiliations/${affiliationId}`,
      request
    );

    return this.affiliatedUserMapper.toModel(response);
  }

  public async removeAffiliateFromMunicipality(
    municipalityId: string,
    affiliationId: string
  ) {
    this.httpClient.delete(
      `/municipalities/${municipalityId}/affiliations/${affiliationId}`
    );
  }
}
