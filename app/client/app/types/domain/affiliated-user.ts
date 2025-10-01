import type {
  Municipality,
  MunicipalityDTO,
  MunicipalityMapper,
} from "./municipality";
import type { User, UserDTO, UserMapper } from "./user";

export type AffiliationStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface AffiliatedUserDTO {
  id: string;
  user: UserDTO;
  municipality: MunicipalityDTO;
  institutionalEmail: string;
  isAdmin: boolean;
  status: AffiliationStatus;
  affiliatedAt: string;
  approver: AffiliatedUserDTO | null;
  approvedAt: string | null;
}

export interface AffiliatedUser {
  id: string;
  user: User;
  municipality: Municipality;
  institutionalEmail: string;
  isAdmin: boolean;
  status: AffiliationStatus;
  affiliatedAt: Date;
  approver: AffiliatedUser | null;
  approvedAt: Date | null;
}

export class AffiliatedUserMapper {
  constructor(
    private readonly userMapper: UserMapper,
    private readonly municipalityMapper: MunicipalityMapper
  ) {}

  public toModel(dto: AffiliatedUserDTO): AffiliatedUser {
    return {
      ...dto,
      user: this.userMapper.toModel(dto.user),
      municipality: this.municipalityMapper.toModel(dto.municipality),
      affiliatedAt: new Date(dto.affiliatedAt),
      approver: dto.approver ? this.toModel(dto.approver) : null,
      approvedAt: dto.approvedAt ? new Date(dto.approvedAt) : null,
    };
  }
}
