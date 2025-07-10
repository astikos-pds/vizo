<script lang="ts" setup>
const { title } = defineProps<{
  title: string;
}>();

const stepper = useSteps();
</script>

<template>
  <section class="size-full flex flex-col items-center p-2">
    <header class="w-full">
      <UButton
        color="neutral"
        variant="ghost"
        size="lg"
        icon="i-lucide-arrow-left"
        class="text-xl"
        v-if="stepper.hasPrev.value"
        @click="stepper.prev"
      />
    </header>
    <main class="w-[65%] md:w-[50%] lg:w-[60%] mt-6 flex flex-col items-center">
      <h1 class="text-2xl font-semibold text-center mb-4">
        {{ title }}
      </h1>

      <span class="text-sm text-center mb-6">
        <slot name="description" />
      </span>

      <slot />
    </main>
  </section>
</template>
