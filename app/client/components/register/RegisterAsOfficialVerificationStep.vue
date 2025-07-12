<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailStore } from "~/stores/email";
import { PIN_INPUT_LENGTH } from "~/utils/constants";
import { useCountdown } from "@vueuse/core";
import { formatTime } from "~/utils/format";

const { t } = useI18n();

const codeSchema = z.object({
  code: z.array(z.string()),
});

type CodeSchema = z.output<typeof codeSchema>;

const form = reactive<CodeSchema>({
  code: [],
});

const store = useEmailStore();
const stepper = useSteps();

const codeExpiresAt = ref(Date.now() + 10 * 60 * 1000);
const expirationTime = computed(
  () => (codeExpiresAt.value - Date.now()) / 1000
);
const { remaining, start } = useCountdown(expirationTime);

onMounted(() => {
  start();
});

const codeExpired = computed<boolean>(() => remaining.value <= 0);

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  if (event.data.code.length !== PIN_INPUT_LENGTH) return;

  console.log(event.data);
  stepper.next();
};

const resend = async () => {
  console.log(store.email);
  const response = {
    expiresAt: Date.now() + 10 * 60 * 1000,
  };

  codeExpiresAt.value = response.expiresAt;
  start();
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
      class="flex flex-col items-center gap-5"
    >
      <UFormField :label="t('registerOfficial.verifyStep.label')" name="code">
        <UPinInput
          v-model="form.code"
          otp
          autofocus
          :length="PIN_INPUT_LENGTH"
        />
      </UFormField>

      <div class="flex flex-col text-center text-sm">
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
        <UButton type="submit" :disabled="codeExpired">{{
          t("registerOfficial.verifyStep.verify")
        }}</UButton>
      </div>
    </UForm>
  </RegisterAsOfficialStep>
</template>
