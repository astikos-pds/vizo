<script lang="ts" setup>
import type { FormSubmitEvent, StepperItem } from "@nuxt/ui";
import ChangePasswordCredentialsStep from "~/components/change-password/ChangePasswordCredentialsStep.vue";

const { t } = useI18n();

useHead({
  title: t("head.changePassword.title"),
  meta: [
    { name: "description", content: t("head.changePassword.description") },
  ],
});

definePageMeta({
  layout: "guest",
});

const steps = ref<StepperItem[]>([
  {
    title: t("pages.changePassword.steps.credentials.title"),
    description: t("pages.changePassword.steps.credentials.description"),
    icon: "i-lucide-mail",
    slot: "credentials" as const,
  },
  {
    title: t("pages.changePassword.steps.verification.title"),
    description: t("pages.changePassword.steps.verification.description"),
    icon: "i-lucide-badge-check",
    slot: "verification" as const,
  },
  {
    title: t("pages.changePassword.steps.details.title"),
    description: t("pages.changePassword.steps.details.description"),
    icon: "i-lucide-book-text",
    slot: "details" as const,
  },
]);

const stepper = useSteps();
stepper.setTotalSteps(steps.value.length);
</script>

<template>
  <LogoGradient />
  <section class="relative size-full flex flex-col items-center">
    <ConfigHeader class="w-full" />
    <section class="size-full flex flex-col items-center overflow-y-auto">
      <UStepper
        :items="steps"
        v-model="stepper.current.value"
        linear
        disabled
        class="size-full"
        :ui="{
          header: 'p-2 border-b border-default',
        }"
      >
        <template #credentials>
          <ChangePasswordCredentialsStep />
        </template>

        <template #verification>
          <ChangePasswordVerificationStep />
        </template>

        <template #details>
          <ChangePasswordDetailsStep />
        </template>
      </UStepper>
    </section>
  </section>
</template>
