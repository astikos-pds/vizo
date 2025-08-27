export interface TokenPairDTO {
  accessToken: string;
  refreshToken: string;
}

export interface EmailVerificationDTO {
  id: string;
  codeLength: number;
  expiresAt: string;
  createdAt: string;
}

export interface UserDTO {
  id: string;
  document: string;
  email: string;
  name: string;
  avatarUrl: string | null;
  credibilityPoints: number;
  createdAt: string;
  updatedAt: string;
}

export interface User {
  id: string;
  document: string;
  email: string;
  name: string;
  avatarUrl: URL | null;
  credibilityPoints: number;
  createdAt: Date;
  updatedAt: Date;
}

export class UserMapper {
  public toModel(dto: UserDTO): User {
    return {
      ...dto,
      avatarUrl: dto.avatarUrl ? new URL(dto.avatarUrl) : null,
      credibilityPoints: dto.credibilityPoints,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt),
    };
  }
}
