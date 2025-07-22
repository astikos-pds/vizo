import type { Citizen, Official } from "~/types/domain";
import type { Api } from "~/types/http";

export interface LoginRequest {
  document: string;
  password: string;
}
export interface TokenPairResponse {
  accessToken: string;
  refreshToken: string;
}

export interface RegisterRequest {
  name: string;
  document: string;
  email: string;
  password: string;
}

export interface RefreshRequest {
  token: string;
}

export interface EmailVerificationRequest {
  email: string;
}
export interface EmailVerificationResponse {
  id: string;
  codeLength: number;
  expiresAt: string;
  createdAt: string;
}

export interface VerificationCodeRequest {
  code: string;
}
export interface VerificationCodeResponse {
  verified: boolean;
}

export const createAuthRepository = (api: Api) => ({
  login: (request: LoginRequest) =>
    api<TokenPairResponse>("/auth/login", {
      method: "POST",
      body: request,
    }),

  registerAsCitizen: (request: RegisterRequest) =>
    api<Citizen>("/auth/citizen/register", {
      method: "POST",
      body: request,
    }),

  registerAsOfficial: (request: RegisterRequest) =>
    api<Official>("/auth/official/register", {
      method: "POST",
      body: request,
    }),

  refresh: (request: RefreshRequest) =>
    api<TokenPairResponse>("/auth/refresh", {
      method: "POST",
      body: request,
    }),

  createVerificationRequest: (request: EmailVerificationRequest) =>
    api<EmailVerificationResponse>("/auth/verification-requests", {
      method: "POST",
      body: request,
    }),

  verifyCode: (requestId: string, request: VerificationCodeRequest) =>
    api<VerificationCodeResponse>(`/auth/verification-requests/${requestId}`, {
      method: "PATCH",
      body: { code: request.code },
    }),
});
