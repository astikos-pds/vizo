import type {
  ChangePasswordRequest,
  CodeRequest,
  EmailVerificationRequest,
  LoginRequest,
  RefreshRequest,
  RegisterRequest,
} from "~/services/auth";
import { useLoggedInUserStore } from "~/stores/user";
import type { Authentication } from "~/types/domain/user";

export const useAuth = () => {
  const accessToken = useCookie("access_token");
  const refreshToken = useCookie("refresh_token");
  const userStore = useLoggedInUserStore();

  const { loading, handle } = useApiHandler();

  const { $authService, $meService } = useNuxtApp();

  async function login(request: LoginRequest) {
    const response = await handle(() => $authService.login(request));

    if (response) {
      authenticate(response);
      const affiliations = await getAffiliations();
      userStore.setAffiliations(affiliations);
    }

    return response;
  }

  async function register(request: RegisterRequest) {
    return await handle(() => $authService.register(request));
  }

  async function logout() {
    authenticate(null);
    userStore.setAffiliations([]);
    userStore.setCurrentAffiliation(null);
  }

  async function refresh(request: RefreshRequest) {
    const response = await handle(() => $authService.refresh(request));

    authenticate(response);

    return response;
  }

  function authenticate(authentication: Authentication | null) {
    accessToken.value = authentication ? authentication.accessToken : null;
    refreshToken.value = authentication ? authentication.refreshToken : null;
    userStore.setUser(authentication ? authentication.user : null);
  }

  async function getAffiliations() {
    const page = await $meService.getMyAffiliations(
      {
        page: 0,
        size: 100,
        status: "APPROVED",
      },
      { Authorization: `Bearer ${accessToken.value}` }
    );
    return page.content;
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
    logout,
    requestEmailVerification,
    verifyEmail,
    changePassword,
  };
};
