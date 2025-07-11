<script lang="ts" setup>
import type { StepperItem } from "@nuxt/ui";

const { t } = useI18n();

useHead({
  title: t("head.registerOfficial.title"),
  meta: [
    { name: "description", content: t("head.registerOfficial.description") },
  ],
});

definePageMeta({
  layout: "guest",
});

const steps = computed(
  () =>
    [
      {
        slot: "email" as const,
        title: t("registerOfficial.steps.email"),
        description: "",
        icon: "i-lucide-mail",
      },
      {
        slot: "verify" as const,
        title: t("registerOfficial.steps.verify"),
        description: "",
        icon: "i-lucide-badge-check",
      },
      {
        slot: "details" as const,
        title: t("registerOfficial.steps.details"),
        description: "",
        icon: "i-lucide-book-open-text",
      },
    ] satisfies StepperItem[]
);

const stepper = useSteps();
stepper.setTotalSteps(steps.value.length);
</script>

<template>
  <section class="relative size-full flex flex-col items-center">
    <ConfigHeader class="w-full" />
    <UStepper
      class="flex-1 w-full gap-0 overflow-y-auto"
      :items="steps"
      v-model="stepper.current.value"
      linear
      disabled
      :ui="{
        header: 'p-3 border-b border-default',
      }"
    >
      <template #email>
        <RegisterAsOfficialEmailStep />
      </template>

      <template #verify>
        <RegisterAsOfficialVerificationStep />
      </template>

      <template #details>
        <RegisterAsOfficialDetailsStep />
      </template>
    </UStepper>
  </section>
  <section
    class="lg:min-w-[45%] xl:min-w-[50%] h-full bg-linear-to-tr from-primary to-neutral-200 dark:to-neutral-500"
  ></section>
</template>
