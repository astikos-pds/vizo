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

  return await app.$api<RegisterAsCitizenResponse>("/auth/register/citizen", {
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
