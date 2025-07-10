<script lang="ts" setup>
const { title } = defineProps<{
  title: string;
}>();
</script>

<template>
  <section
    class="w-[65%] md:w-[50%] lg:w-[60%] p-1 h-full flex flex-col items-center"
  >
    <h1 class="text-2xl font-semibold text-center mt-8 mb-4 lg:mb-6">
      {{ title }}
    </h1>

    <slot />
  </section>
</template>
