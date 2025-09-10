<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailVerification } from "~/composables/use-email-verification";
import { useAffiliationRequestStore } from "~/stores/affiliation-request";

const stepper = useSteps();
const store = useAffiliationRequestStore();

const {
  loading,
  emailVerification,
  remainingTime,
  sendVerificationCodeFor,
  verifyEmailWith,
} = useEmailVerification(store.institutionalEmail);

const codeLength = computed(() =>
  emailVerification.value
    ? emailVerification.value.codeLength
    : PIN_INPUT_LENGTH
);

const codeSchema = z.object({
  code: z.array(z.string()),
});

type CodeSchema = z.output<typeof codeSchema>;

const form = reactive<CodeSchema>({
  code: [],
});

async function sendVerificationCode() {
  await sendVerificationCodeFor("AFFILIATION");
}

onMounted(async () => {
  await sendVerificationCode();
});

const { requestAffiliationToMunicipality } = useAffiliatedUsers();
const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  const code = event.data.code.join("");

  if (code.length !== codeLength.value) return;

  await verifyEmailWith(code);

  if (!store.municipalityId || !store.institutionalEmail) return;

  const response = await requestAffiliationToMunicipality(
    store.municipalityId,
    {
      institutionalEmail: store.institutionalEmail,
    }
  );

  if (!response) {
    toast.clear();

    toast.add({
      title: "Error",
      description: "The affiliation request failed, try again later.",
      color: "error",
    });
    return;
  }

  stepper.next();
};

const onResend = async () => {
  form.code = [];
  emailVerification.value = null;

  await sendVerificationCode();
};
</script>

<template>
  <AffiliationsRequestStep title="Verify with code">
    <template #description>
      Enter the verification code sent to
      <UButtonGroup orientation="horizontal">
        <UBadge color="primary" variant="subtle">{{
          store.institutionalEmail
        }}</UBadge>
        <UButton
          color="neutral"
          variant="outline"
          icon="i-lucide-pencil"
          @click="stepper.goToStart"
        />
      </UButtonGroup>
    </template>

    <div v-if="!emailVerification" class="text-sm size-full text-center">
      Sending e-mail verification...
    </div>

    <UForm
      v-else
      :schema="codeSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4"
    >
      <div class="flex flex-col gap-2">
        <UFormField label="Code" name="code" required>
          <UPinInput v-model="form.code" otp autofocus :length="codeLength" />
        </UFormField>

        <div class="text-center text-sm">
          <span>Code will expire in {{ formatTime(remainingTime) }}</span>
        </div>
      </div>

      <div class="flex gap-1">
        <UButton color="neutral" variant="subtle" @click="onResend"
          >Resend</UButton
        >
        <UButton type="submit" :loading="loading">Verify</UButton>
      </div>
    </UForm>
  </AffiliationsRequestStep>
</template>
