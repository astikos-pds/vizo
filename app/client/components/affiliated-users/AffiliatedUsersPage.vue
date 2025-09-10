<script lang="ts" setup>
defineProps<{
  title: string;
  description: string;
}>();
</script>

<template>
  <div
    class="w-[85%] 2xl:w-[75%] p-3 my-8 flex flex-col gap-5 items-center justify-center"
  >
    <header class="text-center flex flex-col gap-2">
      <h1 class="text-2xl font-semibold">{{ title }}</h1>
      <p class="text-sm">{{ description }}</p>
    </header>

    <main class="w-full flex flex-col items-center">
      <slot />
    </main>
  </div>
</template>
