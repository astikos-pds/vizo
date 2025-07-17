<script lang="ts" setup>
import type { Pageable } from "~/types/http";

const { t } = useI18n();

useHead({
  title: "Vizo | Dashboard",
  meta: [
    {
      name: "description",
      content: "Dashboard",
    },
  ],
});

definePageMeta({
  name: "Dashboard",
  middleware: ["auth", "official", "department"],
});

const pagination = ref<Pageable>({
  page: 0,
  size: 5,
});
</script>

<template>
  <div
    class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-2"
  >
    <!-- <ProblemRate class="col-span-1" />
    <SolvedProblems class="col-span-1" /> -->
    <ProblemTable
      v-model:pagination="pagination"
      class="col-span-1 md:col-span-2 xl:col-span-2"
    />
  </div>
</template>
