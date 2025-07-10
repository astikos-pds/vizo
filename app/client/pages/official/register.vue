<script lang="ts" setup>
import type { StepperItem } from "@nuxt/ui";

useHead({
  title: "Vizo | Sign up as official",
  meta: [{ name: "description", content: "Register as an official in Vizo" }],
});

definePageMeta({
  layout: "guest",
});

const stepper = useTemplateRef("stepper");

const steps = [
  {
    slot: "email" as const,
    title: "E-mail",
    description: "",
    icon: "i-lucide-mail",
  },
  {
    slot: "verify" as const,
    title: "Verify",
    description: "",
    icon: "i-lucide-badge-check",
  },
  {
    slot: "register" as const,
    title: "Register",
    description: "",
    icon: "i-lucide-book-open-text",
  },
] satisfies StepperItem[];
</script>

<template>
  <section
    class="relative size-full flex flex-col items-center overflow-y-auto"
  >
    <ConfigHeader class="w-full" />
    <UStepper
      ref="stepper"
      class="flex-1 w-full"
      :items="steps"
      linear
      :ui="{
        content: 'flex flex-col items-center',
        header: 'p-3 border-b border-default',
      }"
    >
      <template #email>
        <RegisterAsOfficialEmailStep @next="stepper?.next" />
      </template>

      <template #verify>
        <RegisterAsOfficialVerificationStep @next="stepper?.next" />
      </template>
    </UStepper>
  </section>
  <section
    class="lg:min-w-[45%] xl:min-w-[50%] h-full bg-linear-to-tr from-primary to-neutral-200 dark:to-neutral-500"
  ></section>
</template>
