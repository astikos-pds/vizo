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

  const { loading, handle } = useApiHandler();

  async function login(
    request: LoginRequest
  ): Promise<TokenPairResponse | null> {
    const response = await handle<TokenPairResponse>(() =>
      loginUseCase(request)
    );

    if (response) {
      updateTokenPair(response);
    }

    return response;
  }

  async function registerAsCitizen(
    request: RegisterAsCitizenRequest
  ): Promise<RegisterAsCitizenResponse | null> {
    return await handle<RegisterAsCitizenResponse>(() =>
      registerAsCitizenUseCase(request)
    );
  }

  function logout() {
    updateTokenPair({ accessToken: "", refreshToken: "" });
  }

  async function refresh(
    request: RefreshRequest
  ): Promise<TokenPairResponse | null> {
    const response = await handle<TokenPairResponse>(() =>
      refreshUseCase(request)
    );

    if (response) {
      updateTokenPair(response);
    }

    return response;
  }

  async function ensureAuthenticated(): Promise<boolean> {
    if (!accessToken.value) return false;

    if (isTokenExpired(accessToken.value)) {
      if (!refreshToken.value) return false;

      if (isTokenExpired(refreshToken.value)) return false;

      return (await refresh({ token: refreshToken.value })) ? true : false;
    }

    return true;
  }

  function updateTokenPair(tokens: TokenPairResponse) {
    accessToken.value = tokens.accessToken;
    refreshToken.value = tokens.refreshToken;
  }

  return {
    loading,
    accessToken,
    refreshToken,
    login,
    registerAsCitizen,
    logout,
    refresh,
    ensureAuthenticated,
  };
};
