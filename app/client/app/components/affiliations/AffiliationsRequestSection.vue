<script lang="ts" setup>
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import AffiliationsRequestCard from "./AffiliationsRequestCard.vue";
import PaginatedAccordionBody from "../PaginatedAccordionBody.vue";
import type { PageDTO } from "~/types/domain/pagination";

defineProps<{
  items: PageDTO<AffiliatedUser>;
  emptyText: string;
}>();
</script>

<template>
  <PaginatedAccordionBody :items="items" :empty-text="emptyText">
    <AffiliationsRequestCard v-for="item in items.content" v-bind="item" />

    <template #pagination>
      <slot name="pagination" />
    </template>
  </PaginatedAccordionBody>
</template>
