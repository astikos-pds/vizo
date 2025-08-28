import type {
  ChangePasswordRequest,
  CodeRequest,
  EmailVerificationRequest,
  LoginRequest,
  RefreshRequest,
  RegisterRequest,
} from "~/services/auth";
import type { TokenPairDTO } from "~/types/domain/user";

export const useAuth = () => {
  const accessToken = useCookie("access_token");
  const refreshToken = useCookie("refresh_token");

  const { loading, handle } = useApiHandler();

  const { $authService } = useNuxtApp();

  async function login(request: LoginRequest) {
    const response = await handle<TokenPairDTO>(() =>
      $authService.login(request)
    );

    updateTokenPair(response);

    return response;
  }

  async function register(request: RegisterRequest) {
    return await handle(() => $authService.register(request));
  }

  async function logout() {
    updateTokenPair({ accessToken: "", refreshToken: "" });
    useUserStore().setUser(null);
  }

  async function refresh(request: RefreshRequest) {
    const response = await handle(() => $authService.refresh(request));

    updateTokenPair(response);

    return response;
  }

  function updateTokenPair(tokens: TokenPairDTO | null) {
    if (!tokens) return;

    accessToken.value = tokens.accessToken;
    refreshToken.value = tokens.refreshToken;
  }

  async function requestEmailVerification(request: EmailVerificationRequest) {
    return await handle(() => $authService.requestEmailVerification(request));
  }

  async function verifyEmail(
    emailVerificationRequestId: string,
    request: CodeRequest
  ) {
    return await handle(() =>
      $authService.verifyEmail(emailVerificationRequestId, request)
    );
  }

  async function changePassword(request: ChangePasswordRequest) {
    return await handle(() => $authService.changePassword(request));
  }

  return {
    loading,
    accessToken,
    refreshToken,
    login,
    register,
    refresh,
    requestEmailVerification,
    verifyEmail,
    changePassword,
  };
};
