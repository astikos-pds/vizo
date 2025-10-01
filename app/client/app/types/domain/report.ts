import { type Problem, type ProblemDTO, type ProblemMapper } from "./problem";
import { type User, type UserDTO, type UserMapper } from "./user";

export interface ReportDTO {
  id: string;
  user: UserDTO;
  problem: ProblemDTO;
  description: string;
  latitude: number;
  longitude: number;
  imagesUrls: string[];
  credibility: number;
  createdAt: string;
}

export interface Report {
  id: string;
  user: User;
  problem: Problem;
  description: string;
  latitude: number;
  longitude: number;
  imagesUrls: URL[];
  credibility: number;
  createdAt: Date;
}

export class ReportMapper {
  constructor(
    private readonly userMapper: UserMapper,
    private readonly problemMapper: ProblemMapper
  ) {}

  public toModel(dto: ReportDTO): Report {
    return {
      ...dto,
      user: this.userMapper.toModel(dto.user),
      problem: this.problemMapper.toModel(dto.problem),
      imagesUrls: dto.imagesUrls.map((imageUrl) => new URL(imageUrl)),
      createdAt: new Date(dto.createdAt),
    };
  }
}
