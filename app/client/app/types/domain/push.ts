import type { User, UserDTO } from "./user";

export type Platform = "WEB" | "ANDROID" | "IOS";

export interface PushTokenDTO {
  id: number;
  userId: UserDTO["id"];
  token: string;
  platform: Platform;
  createdAt: string;
  lastUsedAt: string;
}

export interface PushToken {
  id: number;
  userId: User["id"];
  token: string;
  platform: Platform;
  createdAt: Date;
  lastUsedAt: Date;
}

export class PushTokenMapper {
  public toModel(dto: PushTokenDTO): PushToken {
    return {
      ...dto,
      createdAt: new Date(dto.createdAt),
      lastUsedAt: new Date(dto.lastUsedAt),
    };
  }
}
