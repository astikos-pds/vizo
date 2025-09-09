import type { VerificationPurpose } from "~/services/auth";
import type { EmailVerification } from "~/types/domain/user";

export const useEmailVerification = (email: string | null) => {
  const emailVerification = ref<EmailVerification | null>(null);

  const expirationTime = computed(() =>
    emailVerification.value
      ? (emailVerification.value.expiresAt.getTime() - Date.now()) / 1000
      : 0
  );
  const { remaining: remainingTime, start } = useCountdown(0);

  const { loading, requestEmailVerification, verifyEmail } = useAuth();

  async function sendVerificationCodeFor(purpose: VerificationPurpose) {
    if (!email) return;

    const response = await requestEmailVerification({
      email,
      purpose: purpose,
    });

    if (!response) return;

    emailVerification.value = response;
    start(expirationTime);
  }

  async function verifyEmailWith(code: string) {
    if (!emailVerification.value) return;

    return await verifyEmail(emailVerification.value.id, {
      code,
    });
  }

  return {
    loading,
    emailVerification,
    remainingTime,
    sendVerificationCodeFor,
    verifyEmailWith,
  };
};
