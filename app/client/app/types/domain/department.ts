import type {
  AffiliatedUser,
  AffiliatedUserDTO,
  AffiliatedUserMapper,
} from "./affiliated-user";
import type {
  Municipality,
  MunicipalityDTO,
  MunicipalityMapper,
} from "./municipality";
import type { ProblemType } from "./problem";

export interface DepartmentDTO {
  id: string;
  municipality: MunicipalityDTO;
  creator: AffiliatedUserDTO;
  name: string;
  colorHex: string;
  iconUrl: string | null;
  scope: ProblemType[];
  createdAt: string;
  updatedAt: string;
}

export interface Department {
  id: string;
  municipality: Municipality;
  creator: AffiliatedUser;
  name: string;
  colorHex: string;
  iconUrl: URL | null;
  scope: ProblemType[];
  createdAt: Date;
  updatedAt: Date;
}

export class DepartmentMapper {
  constructor(
    private readonly municipalityMapper: MunicipalityMapper,
    private readonly affiliatedUserMapper: AffiliatedUserMapper
  ) {}

  public toModel(dto: DepartmentDTO): Department {
    return {
      ...dto,
      municipality: this.municipalityMapper.toModel(dto.municipality),
      creator: this.affiliatedUserMapper.toModel(dto.creator),
      iconUrl: dto.iconUrl ? new URL(dto.iconUrl) : null,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt),
    };
  }
}
