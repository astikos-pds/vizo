<script lang="ts" setup>
defineProps<{
  items: any[];
  emptyText: string;
}>();
</script>

<template>
  <AccordionBody :items="items" :empty-text="emptyText">
    <AffiliationRequestCard v-for="item in items" v-bind="item" />
  </AccordionBody>
</template>
