<script lang="ts" setup>
defineProps<{
  title: string;
  description: string;
}>();
</script>

<template>
  <div class="size-full overflow-auto flex justify-center">
    <div
      class="h-full w-[85%] 2xl:w-[75%] my-10 flex flex-col gap-5 items-center justify-center"
    >
      <header class="text-center flex flex-col gap-2">
        <h1 class="text-2xl font-semibold">{{ title }}</h1>
        <p class="text-sm">{{ description }}</p>
      </header>

      <main class="w-full flex flex-col items-center overflow-hidden">
        <slot />
      </main>
    </div>
  </div>
</template>
