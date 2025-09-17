<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailVerification } from "~/composables/use-email-verification";

const stepper = useSteps();
const store = useEmailStore();

const {
  loading,
  emailVerification,
  remainingTime,
  sendVerificationCodeFor,
  verifyEmailWith,
} = useEmailVerification(store.email);

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
  await sendVerificationCodeFor("PASSWORD_CHANGE");
}

onMounted(async () => {
  await sendVerificationCode();
});

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  const code = event.data.code.join("");

  if (code.length !== codeLength.value) return;

  await verifyEmailWith(code);

  stepper.next();
};

const onResend = async () => {
  form.code = [];
  emailVerification.value = null;

  await sendVerificationCode();
};
</script>

<template>
  <RegisterStep title="Verify with code">
    <template #description>
      Enter the verification code sent to
      <UBadge color="primary" variant="subtle">{{ store.email }}</UBadge
      >.
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
  </RegisterStep>
</template>
