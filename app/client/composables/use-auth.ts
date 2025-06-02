import {
  loginUseCase,
  registerAsCitizenUseCase,
  type LoginRequest,
  type LoginResponse,
  type RegisterAsCitizenRequest,
  type RegisterAsCitizenResponse,
} from "~/services/auth";

export const useAuth = () => {
  const accessToken = useCookie("access_token");
  const refreshToken = useCookie("refresh_token");

  const { loading, error, handle } = useApiHandler();

  async function login(request: LoginRequest) {
    const response = await handle<LoginResponse>(() => loginUseCase(request));

    if (response) {
      accessToken.value = response.accessToken;
      refreshToken.value = response.refreshToken;
    }
  }

  async function registerAsCitizen(request: RegisterAsCitizenRequest) {
    const response = await handle<RegisterAsCitizenResponse>(() =>
      registerAsCitizenUseCase(request)
    );

    if (response) {
      console.log(response);
    }
  }

  return {
    loading,
    error,
    accessToken,
    refreshToken,
    login,
    registerAsCitizen,
  };
};
