<script lang="ts" setup>
defineProps<{
  title: string;
  description: string;
}>();
</script>

<template>
  <div class="w-[75%] 2xl:w-[60%] p-3 my-8 flex flex-col items-center">
    <header class="text-center flex flex-col gap-2">
      <h1 class="text-2xl font-semibold">{{ title }}</h1>
      <p class="text-sm">{{ description }}</p>
    </header>

    <main class="w-full my-8 flex flex-col gap-3">
      <slot />
    </main>
  </div>
</template>
