export interface MunicipalityDTO {
  id: string;
  name: string;
  emailDomain: string;
  iconUrl: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface Municipality {
  id: string;
  name: string;
  emailDomain: string;
  iconUrl: URL | null;
  createdAt: Date;
  updatedAt: Date;
}

export class MunicipalityMapper {
  public toModel(dto: MunicipalityDTO): Municipality {
    return {
      ...dto,
      iconUrl: dto.iconUrl ? new URL(dto.iconUrl) : null,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt),
    };
  }
}
