<script lang="ts" setup>
import type { PageDTO } from "~/types/domain/pagination";

const { items } = defineProps<{
  items: PageDTO<any>;
  emptyText: string;
}>();
</script>

<template>
  <div class="w-full mb-6 border border-default rounded-md">
    <div
      v-if="items.totalElements === 0"
      class="w-full text-sm h-24 flex justify-center items-center"
    >
      {{ emptyText }}
    </div>
    <div v-else class="w-full flex flex-col">
      <main
        class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 p-3"
      >
        <slot />
      </main>
      <footer
        v-if="items.totalPages > 1"
        class="w-full flex justify-center items-center border-t border-default p-2"
      >
        <slot name="pagination" />
      </footer>
    </div>
  </div>
</template>
