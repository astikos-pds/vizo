export interface LoginRequest {
  document: string;
  password: string;
}
export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

export const loginUseCase = async (request: LoginRequest) => {
  const app = useNuxtApp();

  return await app.$api<LoginResponse>("/auth/login", {
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
