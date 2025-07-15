<script lang="ts" setup>
import type { AffiliationRequest } from "~/types/domain";

defineProps<{
  items: AffiliationRequest[];
  emptyText: string;
}>();
</script>

<template>
  <AccordionBody :items="items" :empty-text="emptyText">
    <slot />
  </AccordionBody>
</template>
