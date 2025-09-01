import {
  type EmailVerificationDTO,
  type AuthenticationDTO,
  type UserDTO,
  type UserMapper,
  type AuthenticationMapper,
  EmailVerificationMapper,
} from "~/types/domain/user";
import type { HttpClient } from "~/utils/http";

export type VerificationPurpose =
  | "REGISTRATION"
  | "PASSWORD_CHANGE"
  | "AFFILIATION";

export interface RegisterRequest {
  name: string;
  document: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  document: string;
  password: string;
}

export interface RefreshRequest {
  token: string;
}

export interface EmailVerificationRequest {
  email: string;
  purpose: VerificationPurpose;
}

export interface CodeRequest {
  code: string;
}

export interface ChangePasswordRequest {
  email: string;
  newPassword: string;
}

export class AuthService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly userMapper: UserMapper,
    private readonly authenticationMapper: AuthenticationMapper,
    private readonly emailVerificationMapper: EmailVerificationMapper
  ) {}

  public async register(request: RegisterRequest) {
    const response = await this.httpClient.post<UserDTO>(
      "/auth/register",
      request
    );

    return this.userMapper.toModel(response);
  }

  public async login(request: LoginRequest) {
    const response = await this.httpClient.post<AuthenticationDTO>(
      "/auth/login",
      request
    );

    return this.authenticationMapper.toModel(response);
  }

  public async refresh(request: RefreshRequest) {
    const response = await this.httpClient.post<AuthenticationDTO>(
      "/auth/refresh",
      request
    );

    return this.authenticationMapper.toModel(response);
  }

  public async requestEmailVerification(request: EmailVerificationRequest) {
    const response = await this.httpClient.post<EmailVerificationDTO>(
      "/auth/email-verification-requests",
      request
    );

    return this.emailVerificationMapper.toModel(response);
  }

  public async verifyEmail(
    emailVerificationRequestId: string,
    request: CodeRequest
  ) {
    await this.httpClient.patch<void>(
      `/auth/email-verification-requests/${emailVerificationRequestId}/verify`,
      request
    );
  }

  public async changePassword(request: ChangePasswordRequest) {
    await this.httpClient.patch<void>("/auth/change-password", request);
  }
}
