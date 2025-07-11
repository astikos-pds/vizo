<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useOfficialStore } from "~/stores/official";
import { useSteps } from "~/composables/use-steps";

const { t } = useI18n();

const emailSchema = z.object({
  email: z.string().email(t("register.verification.email")),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useOfficialStore();

const form = reactive<EmailSchema>({
  email: store.email,
});

const stepper = useSteps();

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  console.log(event);
  store.setEmail(event.data.email);
  stepper.next();
};
</script>

<template>
  <RegisterAsOfficialStep :title="t('registerOfficial.emailStep.title')">
    <template #description>
      {{ t("registerOfficial.emailStep.description") }}
    </template>

    <UForm
      :schema="emailSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4 md:gap-3"
    >
      <UFormField
        :label="t('registerOfficial.emailStep.label')"
        name="email"
        class="w-full"
      >
        <UInput
          v-model="form.email"
          type="text"
          :placeholder="t('registerOfficial.emailStep.placeholder')"
          class="w-full"
        />
      </UFormField>

      <UButton type="submit">{{ t("register.submitButton") }}</UButton>
    </UForm>
  </RegisterAsOfficialStep>
</template>
