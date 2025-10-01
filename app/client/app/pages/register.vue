<script lang="ts" setup>
import type { StepperItem } from "@nuxt/ui";

const { t } = useI18n();

useHead({
  title: t("head.register.title"),
  meta: [{ name: "description", content: t("head.register.description") }],
});

definePageMeta({
  layout: "guest",
});

const steps = ref<StepperItem[]>([
  {
    title: t("pages.register.steps.email.title"),
    description: t("pages.register.steps.email.description"),
    icon: "i-lucide-mail",
    slot: "email" as const,
  },
  {
    title: t("pages.register.steps.verification.title"),
    description: t("pages.register.steps.verification.description"),
    icon: "i-lucide-badge-check",
    slot: "verification" as const,
  },
  {
    title: t("pages.register.steps.details.title"),
    description: t("pages.register.steps.details.description"),
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
        <template #email>
          <RegisterEmailStep />
        </template>

        <template #verification>
          <RegisterVerificationStep />
        </template>

        <template #details>
          <RegisterDetailsStep />
        </template>
      </UStepper>
    </section>
  </section>
  <LogoGradient />
</template>
