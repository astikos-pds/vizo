<script lang="ts" setup>
defineProps<{
  items: any[];
  emptyText: string;
}>();
</script>

<template>
  <div class="w-full px-4 py-2 mb-6">
    <div
      v-if="items.length === 0"
      class="w-full text-sm h-24 flex justify-center items-center border border-default rounded-md"
    >
      {{ emptyText }}
    </div>
    <div
      v-else
      class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3"
    >
      <slot />
    </div>
  </div>
</template>
