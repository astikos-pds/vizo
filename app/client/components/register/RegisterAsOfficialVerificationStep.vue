<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailStore } from "~/stores/email";
import { PIN_INPUT_LENGTH } from "~/utils/constants";
import { useCountdown } from "@vueuse/core";
import { formatTime } from "~/utils/format";
import type { EmailVerificationResponse } from "~/services/auth";

const { t } = useI18n();

const codeSchema = z.object({
  code: z.array(z.string()),
});

type CodeSchema = z.output<typeof codeSchema>;

const form = reactive<CodeSchema>({
  code: [],
});

const verificationRequest = ref<EmailVerificationResponse>();

const expirationTime = computed(
  () =>
    (Date.parse(verificationRequest.value?.expiresAt ?? Date.now().toString()) -
      Date.now()) /
    1000
);
const { remaining, start } = useCountdown(expirationTime);
const codeExpired = computed<boolean>(() => remaining.value <= 0);

const alreadyVerified = ref<boolean>(false);

const { loading, createVerificationRequest, verifyCode } = useAuth();
const store = useEmailStore();
const toast = useToast();

async function sendVerificationRequest() {
  const response = await createVerificationRequest({
    email: store.email,
  });

  if (!response) {
    const toastStore = useToastStore();

    toast.update(toastStore.toastId, {
      title: t("toast.warning.title"),
      description: t("registerOfficial.verifyStep.toast.request.description"),
      color: "warning",
    });

    if (toastStore.status === 409) alreadyVerified.value = true;

    return;
  }

  verificationRequest.value = response;

  start();
}

onMounted(async () => {
  await sendVerificationRequest();
});

const stepper = useSteps();

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  if (event.data.code.length !== verificationRequest.value?.codeLength) return;

  const response = await verifyCode({
    requestId: verificationRequest.value.id,
    code: event.data.code.join(""),
  });

  if (!response) {
    toast.update(useToastStore().toastId, {
      title: t("toast.error.title"),
      description: t("registerOfficial.verifyStep.toast.verify.description"),
      color: "error",
    });
    return;
  }

  stepper.next();
};

const resend = async () => {
  form.code = [];

  await sendVerificationRequest();
};
</script>

<template>
  <RegisterAsOfficialStep :title="t('registerOfficial.verifyStep.title')">
    <template #description>
      <i18n-t keypath="registerOfficial.verifyStep.description">
        <template #email>
          <UBadge variant="subtle">{{ store.email }}</UBadge>
        </template>
      </i18n-t>
    </template>

    <UForm
      :schema="codeSchema"
      :state="form"
      @submit="onSubmit"
      :disabled="loading || !verificationRequest"
      class="flex flex-col items-center gap-5"
    >
      <UFormField :label="t('registerOfficial.verifyStep.label')" name="code">
        <UPinInput
          v-model="form.code"
          otp
          autofocus
          :length="verificationRequest?.codeLength ?? PIN_INPUT_LENGTH"
        />
      </UFormField>

      <div
        v-if="!loading && verificationRequest"
        class="flex flex-col text-center text-sm"
      >
        <span>{{
          t("registerOfficial.verifyStep.expiresIn", {
            time: formatTime(remaining),
          })
        }}</span>
        <span v-if="codeExpired" class="text-warning">{{
          t("registerOfficial.verifyStep.expired")
        }}</span>
      </div>

      <div class="flex gap-2">
        <UButton color="neutral" variant="outline" @click="resend">{{
          t("registerOfficial.verifyStep.resend")
        }}</UButton>
        <UButton
          type="submit"
          :disabled="codeExpired || !verificationRequest"
          :loading="loading"
          >{{ t("registerOfficial.verifyStep.verify") }}</UButton
        >
        <UButton
          v-if="alreadyVerified"
          color="neutral"
          variant="ghost"
          icon="i-lucide-arrow-right"
          @click="stepper.next"
          >{{ t("registerOfficial.verifyStep.continue") }}</UButton
        >
      </div>
    </UForm>
  </RegisterAsOfficialStep>
</template>
