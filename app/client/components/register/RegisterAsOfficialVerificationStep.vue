<script lang="ts" setup>
import type { FormSubmitEvent } from '@nuxt/ui';
import z from 'zod';
import { useOfficialStore } from '~/stores/official';
import { PIN_INPUT_LENGTH } from '~/utils/constants';

const codeSchema = z.object({
  code: z.array(z.string()).length(PIN_INPUT_LENGTH, "Code has to have 6 digits."),
});

type CodeSchema = z.output<typeof codeSchema>;

const form = reactive<CodeSchema>({
  code: [],
});

const store = useOfficialStore();
const stepper = useSteps();

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  console.log(event.data);
  stepper.next()
};

const resend = async () => {
  console.log(store.email)
}
</script>

<template>
  <RegisterAsOfficialStep title="Verify your e-mail">
    <template #description>
      We sent a verification code to <UBadge variant="subtle">{{ store.email }}</UBadge>. Copy and paste it below.
    </template>

    <UForm :schema="codeSchema" :state="form" @submit="onSubmit" class="flex flex-col items-center gap-5">
      <UFormField label="Verification code" name="code">
        <UPinInput v-model="form.code" otp autofocus :length="PIN_INPUT_LENGTH" />
      </UFormField>

      <div class="flex gap-2">
        <UButton color="neutral" variant="outline" @click="resend">Resend</UButton>
        <UButton type="submit">Verify</UButton>
      </div>
    </UForm>
  </RegisterAsOfficialStep>
</template>