<script lang="ts" setup>
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import AffiliationsApprovedCard from "./AffiliationsApprovedCard.vue";
import type { PageDTO } from "~/types/domain/pagination";
import PaginatedAccordionBody from "../PaginatedAccordionBody.vue";
import AffiliationsRequestedCard from "./AffiliationsRequestedCard.vue";

type Purpose = "REQUESTED" | "APPROVED";

defineProps<{
  items: PageDTO<AffiliatedUser>;
  emptyText: string;
  purpose: Purpose;
}>();
</script>

<template>
  <PaginatedAccordionBody :items="items" :empty-text="emptyText">
    <div v-for="item in items.content">
      <AffiliationsApprovedCard v-if="purpose === 'APPROVED'" v-bind="item" />
      <AffiliationsRequestedCard
        v-else-if="purpose === 'REQUESTED'"
        v-bind="item"
      />
    </div>

    <template #pagination>
      <slot name="pagination" />
    </template>
  </PaginatedAccordionBody>
</template>
