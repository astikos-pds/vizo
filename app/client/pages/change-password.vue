<script lang="ts" setup>
import type { FormSubmitEvent, StepperItem } from "@nuxt/ui";
import ChangePasswordCredentialsStep from "~/components/change-password/ChangePasswordCredentialsStep.vue";

const { t } = useI18n();

useHead({
  title: t("head.registerCitizen.title"),
  meta: [
    { name: "description", content: t("head.registerCitizen.description") },
  ],
});

definePageMeta({
  layout: "guest",
});

const steps = ref<StepperItem[]>([
  {
    title: "Credentials",
    description: "Check your e-mail and CPF",
    icon: "i-lucide-mail",
    slot: "credentials" as const,
  },
  {
    title: "Verification",
    description: "Paste the code sent to e-mail",
    icon: "i-lucide-badge-check",
    slot: "verification" as const,
  },
  {
    title: "New password",
    description: "Define a new password",
    icon: "i-lucide-book-text",
    slot: "details" as const,
  },
]);

const stepper = useSteps();
stepper.setTotalSteps(steps.value.length);
</script>

<template>
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
  <LogoGradient />
</template>
