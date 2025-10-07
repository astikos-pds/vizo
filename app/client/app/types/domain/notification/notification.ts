import type { User, UserDTO, UserMapper } from "../user";
import type { DomainEvent, DomainEventDTO, EventMapper } from "./event";

export type NotificationType = "NEW_PROBLEM";

export interface NotificationDTO {
  id: string;
  recipient: UserDTO;
  type: NotificationType;
  payload: DomainEventDTO;
  read: boolean;
  createdAt: string;
}

export interface Notification {
  id: string;
  recipient: User;
  type: NotificationType;
  payload: DomainEvent;
  read: boolean;
  createdAt: Date;
}

export class NotificationMapper {
  public constructor(
    private readonly userMapper: UserMapper,
    private readonly eventMapper: EventMapper
  ) {}

  public toModel(dto: NotificationDTO): Notification {
    return {
      ...dto,
      payload: this.eventMapper.toModel(dto.payload),
      recipient: this.userMapper.toModel(dto.recipient),
      createdAt: new Date(dto.createdAt),
    };
  }
}
