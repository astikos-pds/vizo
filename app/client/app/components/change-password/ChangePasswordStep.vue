<script lang="ts" setup>
defineProps<{
  title: string;
  description?: string;
}>();
</script>

<template>
  <section class="size-full flex flex-col justify-between">
    <main class="p-3 flex-1 flex flex-col items-center gap-8">
      <div class="flex flex-col gap-1 text-center">
        <h1 class="text-2xl font-semibold">{{ title }}</h1>
        <span class="text-sm">
          <slot name="description">
            {{ description }}
          </slot>
        </span>
      </div>
      <div class="h-full w-[70%] md:w-[50%] lg:w-[65%] 2xl:w-[50%]">
        <slot />
      </div>
    </main>

    <footer class="py-8 text-center text-sm">
      <span
        >Já tem uma conta?
        <NuxtLink class="text-primary" to="/login">Entre aqui.</NuxtLink></span
      >
    </footer>
  </section>
</template>
