<script setup lang="ts">
import { ref } from "vue";
import VueApexCharts from "vue3-apexcharts";
import type { ProblemStatus, ProblemType } from "~/types/domain/problem";

const { t } = useI18n();

const filters = reactive<{
  start: Date;
  end: Date;
  statuses: ProblemStatus[];
  types: ProblemType[];
}>({
  start: new Date(),
  end: new Date(),
  statuses: [],
  types: [],
});

const { currentAssignment } = useLoggedInUserStore();

const statuses = ref<ProblemStatus[]>([
  "ANALYSIS",
  "IN_PROGRESS",
  "RESOLVED",
  "REJECTED",
]);

const types = ref<ProblemType[]>(
  currentAssignment ? currentAssignment.department.scope : []
);

const { getProblemsInScopeStatistics } = useDepartments();

const { data: statistics } = await getProblemsInScopeStatistics(
  currentAssignment ? currentAssignment.department.municipality.id : "",
  currentAssignment ? currentAssignment.department.id : "",
  filters
);

const series = ref([
  {
    name: t("components.dashboard.problemsStatistics"),
    data: [] as { x: number; y: number }[],
  },
]);

const xaxisLimits = computed(() => {
  if (!statistics.value) return { min: 0, max: 0 };

  const allX = statistics.value.map((d) =>
    new Date(d.date.toISOString().split("T")[0]).getTime()
  );

  const min = Math.min(...allX);
  const max = Math.max(...allX);

  const paddingDays = 5 * 24 * 60 * 60 * 1000;

  let paddedMin = min - paddingDays;
  let paddedMax = max + paddingDays;

  const minRange = 3 * 24 * 60 * 60 * 1000;
  const range = paddedMax - paddedMin;

  if (range < minRange) {
    const center = (min + max) / 2;
    paddedMin = center - minRange / 2;
    paddedMax = center + minRange / 2;
  }

  return {
    min: paddedMin,
    max: paddedMax,
  };
});

const chartOptions = ref({
  chart: {
    type: "line",
  },
  xaxis: {
    type: "datetime",
    min: xaxisLimits.value.min,
    max: xaxisLimits.value.max,
  },
  yaxis: {
    min: 0,
    max: (max: number) => {
      return Math.max(max, 3);
    },
  },
});

watch(
  statistics,
  (newStatistics) => {
    if (!newStatistics) return;

    series.value[0].data = newStatistics.map((d) => ({
      x: new Date(d.date.toISOString().split("T")[0]).getTime(),
      y: d.count,
    }));

    chartOptions.value.xaxis.type = "datetime";
  },
  { immediate: true }
);
</script>

<template>
  <section class="flex flex-col">
    <header class="p-5 pb-1 flex flex-col gap-3">
      <h1 class="font-semibold text-lg">
        {{ t("components.dashboard.problemsStatistics") }}
      </h1>

      <div class="flex gap-2">
        <RangePicker v-model="filters" />

        <USelect
          v-model="filters.statuses"
          multiple
          :items="statuses"
          :placeholder="t('components.dashboard.filterByStatus')"
          icon="i-lucide-funnel"
          class="min-w-48"
        />

        <USelect
          v-model="filters.types"
          multiple
          :items="types"
          :placeholder="t('components.dashboard.filterByProblemType')"
          icon="i-lucide-list-filter"
          class="min-w-48"
        />
      </div>
    </header>

    <VueApexCharts
      :options="chartOptions"
      :series="series"
      type="line"
      height="250"
    />
  </section>
</template>
