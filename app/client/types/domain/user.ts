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

export interface AuthenticationDTO {
  accessToken: string;
  refreshToken: string;
  user: UserDTO;
}

export interface Authentication {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export class AuthenticationMapper {
  constructor(private readonly userMapper: UserMapper) {}

  public toModel(dto: AuthenticationDTO): Authentication {
    return {
      ...dto,
      user: this.userMapper.toModel(dto.user),
    };
  }
}
