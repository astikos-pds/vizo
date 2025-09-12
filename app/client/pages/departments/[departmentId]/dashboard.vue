<script lang="ts" setup>
import ProblemsTable from "~/components/problems/ProblemsTable.vue";
import type { Pagination } from "~/types/domain/pagination";

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
  middleware: ["auth", "assigned"],
});

const { currentAssignment } = useLoggedInUserStore();

const { getProblemsInScope } = useDepartments();

const pagination = ref<Pagination>({
  page: 0,
  size: 5,
});

const { data: problems } = await getProblemsInScope(
  currentAssignment ? currentAssignment.department.municipality.id : "",
  currentAssignment ? currentAssignment.department.id : "",
  pagination.value
);
</script>

<template>
  <div
    class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-2"
  >
    <!-- <ProblemRate class="col-span-1" />
    <SolvedProblems class="col-span-1" /> -->
    <ProblemsTable
      v-if="problems"
      v-model:problems="problems"
      v-model:pagination="pagination"
      class="col-span-1 md:col-span-2 xl:col-span-2"
    />
  </div>
</template>
