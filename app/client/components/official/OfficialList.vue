<script lang="ts" setup>
import type { Official } from "~/types/domain";

defineProps<{
  items: Official[];
}>();
</script>

<template>
  <div class="pl-4 mb-6 flex flex-col gap-2">
    <div
      v-if="items.length === 0"
      class="text-sm flex justify-center items-center h-24 border border-default rounded-md"
    >
      No officials found.
    </div>
    <div v-else class="w-full" v-for="(official, i) in items">
      <OfficialProfile
        :key="official.id"
        v-bind="official"
        class="w-full mb-2"
      />
      <USeparator v-if="i < items.length - 1" />
    </div>
  </div>
</template>
