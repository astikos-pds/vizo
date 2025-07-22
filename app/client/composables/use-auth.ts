import {
  createAuthRepository,
  type EmailVerificationRequest,
  type EmailVerificationResponse,
  type LoginRequest,
  type RefreshRequest,
  type RegisterRequest,
  type TokenPairResponse,
  type VerificationCodeRequest,
  type VerificationCodeResponse,
} from "~/repositories/auth";
import type { Citizen, Official } from "~/types/domain";

export const useAuth = () => {
  const accessToken = useCookie("access_token");
  const refreshToken = useCookie("refresh_token");

  const { loading, handle } = useApiHandler();

  const { $api } = useNuxtApp();
  const authRepository = createAuthRepository($api);

  async function login(request: LoginRequest) {
    const response = await handle<TokenPairResponse>(() =>
      authRepository.login(request)
    );

    if (response) {
      updateTokenPair(response);
    }

    return response;
  }

  async function registerAsCitizen(request: RegisterRequest) {
    return await handle<Citizen>(() =>
      authRepository.registerAsCitizen(request)
    );
  }

  async function registerAsOfficial(request: RegisterRequest) {
    return await handle<Official>(() =>
      authRepository.registerAsOfficial(request)
    );
  }

  async function logout() {
    updateTokenPair({ accessToken: "", refreshToken: "" });
    useUserStore().setUser(null);
  }

  async function refresh(
    request: RefreshRequest
  ): Promise<TokenPairResponse | null> {
    const response = await handle<TokenPairResponse>(() =>
      authRepository.refresh(request)
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
      authRepository.createVerificationRequest(request)
    );
  }

  async function verifyCode(
    requestId: string,
    request: VerificationCodeRequest
  ) {
    return await handle<VerificationCodeResponse>(() =>
      authRepository.verifyCode(requestId, request)
    );
  }

  return {
    loading,
    accessToken,
    refreshToken,
    login,
    registerAsCitizen,
    registerAsOfficial,
    logout,
    refresh,
    createVerificationRequest,
    verifyCode,
  };
};
