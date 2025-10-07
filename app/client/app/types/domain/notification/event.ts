import type { ProblemType } from "../problem";

export type DomainEventDTO = NewProblemEventDTO;

export type DomainEvent = NewProblemEvent;

export interface NewProblemEventDTO {
  problemId: string;
  problemType: ProblemType;
  problemLatitude: number;
  problemLongitude: number;
  firstReportDescription: string;
  occuredAt: string;
}

export interface NewProblemEvent {
  problemId: string;
  problemType: ProblemType;
  problemLatitude: number;
  problemLongitude: number;
  firstReportDescription: string;
  occuredAt: Date;
}

export class EventMapper {
  public toModel(dto: DomainEventDTO): DomainEvent {
    return {
      ...dto,
      occuredAt: new Date(dto.occuredAt),
    };
  }
}
