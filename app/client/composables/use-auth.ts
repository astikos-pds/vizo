import {
  loginUseCase,
  registerAsCitizenUseCase,
  type LoginRequest,
  type TokenPairResponse,
  type RegisterAsCitizenRequest,
  type RegisterAsCitizenResponse,
  type RefreshRequest,
  refreshUseCase,
  type EmailVerificationRequest,
  type EmailVerificationResponse,
  createVerificationRequestUseCase,
  type VerificationCodeRequest,
  type VerificationCodeResponse,
  verifyCodeUseCase,
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

  async function logout() {
    updateTokenPair({ accessToken: "", refreshToken: "" });

    await navigateTo("/login");
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

  function updateTokenPair(tokens: TokenPairResponse) {
    accessToken.value = tokens.accessToken;
    refreshToken.value = tokens.refreshToken;
  }

  async function createVerificationRequest(request: EmailVerificationRequest) {
    return await handle<EmailVerificationResponse>(() =>
      createVerificationRequestUseCase(request)
    );
  }

  async function verifyCode(request: VerificationCodeRequest) {
    return await handle<VerificationCodeResponse>(() =>
      verifyCodeUseCase(request)
    );
  }

  return {
    loading,
    accessToken,
    refreshToken,
    login,
    registerAsCitizen,
    logout,
    refresh,
    createVerificationRequest,
    verifyCode,
  };
};
