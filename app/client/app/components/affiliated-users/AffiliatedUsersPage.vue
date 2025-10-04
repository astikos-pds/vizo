<script lang="ts" setup>
defineProps<{
  title: string;
  description: string;
}>();
</script>

<template>
  <CommonPage :title="title" with-padding>
    <div class="size-full flex flex-col items-center mt-6">
      <div
        class="h-full w-full lg:w-[70%] 2xl:w-[55%] flex flex-col gap-5 items-center"
      >
        <header class="text-center flex flex-col gap-2">
          <h1 class="text-2xl font-semibold">{{ title }}</h1>
          <p class="text-sm">{{ description }}</p>
        </header>

        <main class="w-full flex flex-col items-center">
          <slot />
        </main>
      </div>
    </div>
  </CommonPage>
</template>
