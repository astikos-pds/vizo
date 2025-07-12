export interface LoginRequest {
  document: string;
  password: string;
}
export interface TokenPairResponse {
  accessToken: string;
  refreshToken: string;
}

export const loginUseCase = async (request: LoginRequest) => {
  const app = useNuxtApp();

  return await app.$api<TokenPairResponse>("/auth/login", {
    method: "POST",
    body: { ...request },
  });
};

export interface RegisterAsCitizenRequest {
  name: string;
  document: string;
  email: string;
  password: string;
}
export interface RegisterAsCitizenResponse {
  id: string;
  email: string;
  name: string;
  credibilityPoints: number;
  createdAt: string;
  updatedAt: string;
}

export const registerAsCitizenUseCase = async (
  request: RegisterAsCitizenRequest
) => {
  const app = useNuxtApp();

  return await app.$api<RegisterAsCitizenResponse>("/auth/citizen/register", {
    method: "POST",
    body: { ...request },
  });
};

export interface RefreshRequest {
  token: string;
}

export const refreshUseCase = async (request: RefreshRequest) => {
  const app = useNuxtApp();

  return await app.$api<TokenPairResponse>("/auth/refresh", {
    method: "POST",
    body: { ...request },
  });
};

export interface EmailVerificationRequest {
  email: string;
}
export interface EmailVerificationResponse {
  id: string;
  codeLength: number;
  expiresAt: string;
  createdAt: string;
}

export const createVerificationRequestUseCase = async (
  request: EmailVerificationRequest
) => {
  const app = useNuxtApp();

  return await app.$api<EmailVerificationResponse>(
    "/auth/verification-requests",
    {
      method: "POST",
      body: request,
    }
  );
};

export interface VerificationCodeRequest {
  requestId: string,
  code: string;
}
export interface VerificationCodeResponse {
  verified: boolean;
}

export const verifyCodeUseCase = async (request: VerificationCodeRequest) => {
  const app = useNuxtApp();

  return await app.$api<VerificationCodeResponse>(
    `/auth/verification-requests/${request.requestId}`,
    {
      method: "PATCH",
      body: {
        code: request.code
      },
    }
  );
}
