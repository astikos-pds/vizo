<script lang="ts" setup>
const { title } = defineProps<{
  title: string;
}>();
</script>

<template>
  <section
    class="w-[65%] md:w-[50%] lg:w-[55%] p-1 h-full flex flex-col items-center"
  >
    <h1 class="text-3xl font-semibold text-center mt-8">
      {{ title }}
    </h1>

    <slot />
  </section>
</template>
