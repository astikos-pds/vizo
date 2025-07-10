<script lang="ts" setup>
import type { FormSubmitEvent } from '@nuxt/ui';
import z from 'zod';
import { useOfficialStore } from '~/stores/official';
import { PIN_INPUT_LENGTH } from '~/utils/constants';

const emit = defineEmits<{
  (e: "next"): void;
}>();

const codeSchema = z.object({
  code: z.array(z.string()).length(PIN_INPUT_LENGTH, "Code has to have 6 digits."),
});

type CodeSchema = z.output<typeof codeSchema>;

const form = reactive<CodeSchema>({
  code: [],
});

const store = useOfficialStore();

const onSubmit = async (event: FormSubmitEvent<CodeSchema>) => {
  console.log(event.data);
  emit("next");
};
</script>

<template>
  <RegisterAsOfficialStep title="Verify your e-mail">
    <span class="text-sm text-center mb-4">We sent a verification code to <UBadge variant="subtle">{{ store.email }}</UBadge>. Copy and paste it below.</span>

    <UForm :schema="codeSchema" :state="form" @submit="onSubmit" class="flex flex-col items-center gap-5">
      <UFormField label="Verification code" name="code">
        <UPinInput v-model="form.code" otp :length="PIN_INPUT_LENGTH" />
      </UFormField>

      <UButton type="submit">Verify</UButton>
    </UForm>
  </RegisterAsOfficialStep>
</template>