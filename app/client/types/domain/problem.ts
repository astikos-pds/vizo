export type ProblemType =
  | "POTHOLE"
  | "POOR_LIGHTING"
  | "GARBAGE"
  | "FLOODING"
  | "OTHER";

export type ProblemStatus =
  | "ANALYSIS"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "REJECTED";

export interface ProblemDTO {
  id: string;
  latitude: number;
  longitude: number;
  type: ProblemType;
  status: ProblemStatus;
  accumulatedCredibility: number;
  validated: boolean;
  createdAt: string;
  updatedAt: string;
  firstReportedAt: string;
  lastReportedAt: string;
  resolvedAt: string;
}

export interface Problem {
  id: string;
  latitude: number;
  longitude: number;
  type: ProblemType;
  status: ProblemStatus;
  accumulatedCredibility: number;
  validated: boolean;
  createdAt: Date;
  updatedAt: Date;
  firstReportedAt: Date;
  lastReportedAt: Date;
  resolvedAt: Date;
}

export class ProblemMapper {
  public toModel(dto: ProblemDTO): Problem {
    return {
      ...dto,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt),
      firstReportedAt: new Date(dto.firstReportedAt),
      lastReportedAt: new Date(dto.lastReportedAt),
      resolvedAt: new Date(dto.resolvedAt),
    };
  }
}
