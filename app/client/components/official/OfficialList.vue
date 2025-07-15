<script lang="ts" setup>
import type { Official } from "~/types/domain";

defineProps<{
  items: Official[];
}>();
</script>

<template>
  <div class="px-4 mb-6 flex flex-col gap-2">
    <div class="w-full" v-for="(official, i) in items">
      <OfficialProfile
        :key="official.id"
        v-bind="official"
        class="w-full mb-2"
      />
      <USeparator v-if="i < items.length - 1" />
    </div>
  </div>
</template>
