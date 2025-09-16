<script lang="ts" setup>
import RadialBar from "~/components/dashboard/RadialBar.vue";
import ProblemPercentage from "~/components/dashboard/ProblemsPercentage.vue";
import ProblemRate from "~/components/dashboard/ProblemRate.vue";
import InfoCard from "~/components/dashboard/InfoCard.vue";
import ProblemsTable from "~/components/problems/ProblemsTable.vue";
import type { Page, Pagination } from "~/types/domain/pagination";
import type { Problem } from "~/types/domain/problem";

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

/* definePageMeta({
  name: "Dashboard",
  middleware: ["auth", "assigned"],
}); */

const { currentAssignment } = useLoggedInUserStore();

const { getProblemsInScope } = useDepartments();

const pagination = ref<Pagination>({
  page: 0,
  size: 5,
});

const problems: Page<Problem> = <Page<Problem>>({
  content: [{
    id: "",
    latitude: 0,
    longitude: 0,
    type: "POTHOLE",
    status: "REJECTED",
    accumulatedCredibility: 0,
    validated: false,
    createdAt: new Date(),
    updatedAt: new Date(),
    firstReportedAt: new Date(),
    lastReportedAt: new Date(),
    resolvedAt: new Date()
  }
  ],
  totalElements: 0,
  totalPages: 0,
  size: 5,
  page: 0,
  map: function <R>(mapper: (t: Problem) => R): Page<R> {
    throw new Error("Function not implemented.");
  }
});

/* const { data: problems } = await getProblemsInScope(
  currentAssignment ? currentAssignment.department.municipality.id : "",
  currentAssignment ? currentAssignment.department.id : "",
  pagination.value
); */
</script>

<template>
  <div class="grid grid-cols-4">
    <InfoCard value="765" label="Problemas Resolvidos" desc="nos últimos 30 dias" color="bg-gradient-to-tr from-green-600 to-green-300 dark:to-green-400" />
    <InfoCard value="23" label="Problemas a Resolver" desc="" color="bg-gradient-to-tr from-orange-600 to-yellow-300" />
    <InfoCard value="23" label="Problemas em Análise" desc="" color="bg-gradient-to-tr from-yellow-500 to-yellow-300" />
    <InfoCard value="23" label="Problemas Rejeitados" desc="nos últimos 30 dias" color="bg-gradient-to-tr from-red-600 to-red-300 dark:to-red-400" />
  </div>

  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-1">
    <ProblemRate class="col-span-1 md:col-span-2 xl:col-span-1"/>
  </div>

  <div class="flex gap-6 p-4 sm:p-6 md:p-8 items-start">
    <ProblemsTable
      v-if="problems"
      v-model:problems="problems"
      v-model:pagination="pagination"
      class="w-full xl:w-[70%]"
    />
    <ProblemPercentage class="w-full xl:w-[30%] self-start" />
  </div>
</template>