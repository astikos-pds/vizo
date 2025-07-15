<script lang="ts" setup>
import type { MunicipalityAffiliation } from "~/services/users";

defineProps<{
  items: MunicipalityAffiliation[];
  emptyText: string;
}>();
</script>

<template>
  <AccordionBody :items="items" :empty-text="emptyText">
    <template v-for="item in items">
      <slot name="item" :item="item" />
    </template>
  </AccordionBody>
</template>
