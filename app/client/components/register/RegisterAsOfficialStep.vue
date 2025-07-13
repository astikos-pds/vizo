<script lang="ts" setup>
import { useSteps } from "~/composables/use-steps";

const { t } = useI18n();

const { title } = defineProps<{
  title: string;
}>();

const stepper = useSteps();
</script>

<template>
  <section class="size-full flex flex-col items-center p-2">
    <header class="w-full flex justify-between">
      <UButton
        color="neutral"
        variant="ghost"
        size="lg"
        icon="i-lucide-arrow-left"
        class="text-xl"
        v-if="stepper.hasPrev.value && stepper.hasNext.value"
        @click="stepper.prev"
      />
    </header>
    <main
      class="w-[65%] md:w-[50%] lg:w-[60%] flex-1 mt-6 flex flex-col items-center"
    >
      <h1 class="text-2xl font-semibold text-center mb-4">
        {{ title }}
      </h1>

      <span class="text-sm text-center mb-6">
        <slot name="description" />
      </span>

      <slot />
    </main>
    <footer class="w-full p-1 flex justify-center items-center">
      <span class="text-sm"
        >{{ t("register.alreadyHaveAccount") }}
        <NuxtLink to="/login" class="text-primary">{{
          t("register.logInHere")
        }}</NuxtLink></span
      >
    </footer>
  </section>
</template>
