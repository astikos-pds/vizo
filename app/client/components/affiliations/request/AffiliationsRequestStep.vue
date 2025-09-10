<script lang="ts" setup>
defineProps<{
  title: string;
  description?: string;
}>();
</script>

<template>
  <section class="size-full flex flex-col items-center">
    <div
      class="w-[65%] md:w-[50%] lg:w-[60%] flex-1 mt-6 flex flex-col items-center"
    >
      <h1 class="text-2xl font-semibold text-center mb-2">
        {{ title }}
      </h1>

      <span class="text-sm text-center mb-6">
        <slot name="description">
          {{ description }}
        </slot>
      </span>

      <slot />
    </div>
  </section>
</template>
