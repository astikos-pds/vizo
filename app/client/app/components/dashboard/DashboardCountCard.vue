<script setup lang="ts">
import type { Badge } from "~/types/ui";

defineProps<{
  count: number;
  label: string;
  color: Badge["color"];
}>();
</script>

<template>
  <UButton
    variant="subtle"
    :color="color"
    class="h-40 flex justify-start items-end p-4"
  >
    <div class="flex gap-3 items-center">
      <span class="text-4xl">{{ count }}</span>
      <span class="text-2xl">{{ label }}</span>
    </div>
  </UButton>
</template>
