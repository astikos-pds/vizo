<script lang="ts" setup>
import ProblemsTable from "~/components/problems/ProblemsTable.vue";
import type { Pagination } from "~/types/domain/pagination";
import type { ProblemStatus } from "~/types/domain/problem";
import type { Badge } from "~/types/ui";

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

const { countProblemsInScopeByStatus } = useDepartments();

const { data: countsByStatus } = await countProblemsInScopeByStatus(
  currentAssignment ? currentAssignment.department.municipality.id : "",
  currentAssignment ? currentAssignment.department.id : ""
);

const mapStatusToColor: Record<ProblemStatus, Badge["color"]> = {
  REJECTED: "error",
  ANALYSIS: "warning",
  IN_PROGRESS: "info",
  RESOLVED: "success",
};
</script>

<template>
  <div
    class="w-full gap-5 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 p-4 md:p-6 overflow-auto"
  >
    <DashboardCountCard
      v-for="countByStatus in countsByStatus"
      :color="mapStatusToColor[countByStatus.status]"
      :label="countByStatus.status"
      :count="countByStatus.count"
      class="col-span-1"
    />

    <DashboardProblemsRate
      class="border border-default rounded-md col-span-4"
    />

    <ProblemsTable
      v-if="problems"
      v-model:problems="problems"
      v-model:pagination="pagination"
      class="col-span-4"
    />
  </div>
</template>
