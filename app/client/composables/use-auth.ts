import {
  loginUseCase,
  registerAsCitizenUseCase,
  type LoginRequest,
  type TokenPairResponse,
  type RegisterAsCitizenRequest,
  type RegisterAsCitizenResponse,
  type RefreshRequest,
  refreshUseCase,
} from "~/services/auth";

export const useAuth = () => {
  const accessToken = useCookie("access_token");
  const refreshToken = useCookie("refresh_token");

  const { loading, error, handle } = useApiHandler();

  async function login(request: LoginRequest) {
    const response = await handle<TokenPairResponse>(() =>
      loginUseCase(request)
    );

    if (response) {
      updateTokenPair(response);
    }
  }

  async function registerAsCitizen(request: RegisterAsCitizenRequest) {
    await handle<RegisterAsCitizenResponse>(() =>
      registerAsCitizenUseCase(request)
    );
  }

  async function refresh(request: RefreshRequest): Promise<boolean> {
    const response = await handle<TokenPairResponse>(() =>
      refreshUseCase(request)
    );

    if (response) {
      updateTokenPair(response);
      return true;
    }

    return false;
  }

  async function ensureAuthenticated(): Promise<boolean> {
    if (!accessToken.value) return false;

    if (isTokenExpired(accessToken.value)) {
      if (!refreshToken.value) return false;

      if (isTokenExpired(refreshToken.value)) return false;

      return await refresh({ refreshToken: refreshToken.value });
    }

    return true;
  }

  function updateTokenPair(tokens: TokenPairResponse) {
    accessToken.value = tokens.accessToken;
    refreshToken.value = tokens.refreshToken;
  }

  return {
    loading,
    error,
    accessToken,
    refreshToken,
    login,
    registerAsCitizen,
    refresh,
    ensureAuthenticated,
  };
};
