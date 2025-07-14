<script lang="ts" setup>
import type { AffiliationRequest } from "~/types/domain";
import AffiliationRequestCard from "./AffiliationRequestCard.vue";

defineProps<{
  items: AffiliationRequest[];
  emptyText: string;
  defaultOpen?: boolean;
}>();
</script>

<template>
  <div
    v-if="items.length === 0"
    class="w-full text-sm h-24 flex justify-center items-center"
  >
    {{ emptyText }}
  </div>
  <div
    v-else
    class="w-full p-3 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3"
  >
    <AffiliationRequestCard v-for="request in items" v-bind="request" />
  </div>
</template>
