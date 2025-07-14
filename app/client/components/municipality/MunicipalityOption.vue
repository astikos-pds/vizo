<script lang="ts" setup>
defineProps<{
  icon: string;
  title: string;
  description: string;
  to: string;
}>();
</script>

<template>
  <NuxtLink :to="to">
    <UCard>
      <div class="relative h-full flex items-center gap-4 p-2">
        <UIcon :name="icon" mode="svg" class="size-7" />
        <div>
          <h2 class="text-xl font-semibold">{{ title }}</h2>
          <p class="text-sm">{{ description }}</p>
        </div>
      </div>
    </UCard>
  </NuxtLink>
</template>
