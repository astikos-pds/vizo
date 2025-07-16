<script lang="ts" setup>
import { ref, computed } from "vue";
import VueApexCharts from "vue3-apexcharts";
import { useI18n } from "vue-i18n";
import type { ApexOptions } from "apexcharts";
const { t, locale } = useI18n();

const items = ref([
  {
    label: t("filter.today"),
  },
  {
    label: t("filter.week"),
  },
  {
    label: t("filter.month"),
  },
  {
    label: t("filter.year"),
  },
]);

const selectedItem = ref(items.value[0]);

const series = computed(() => [
  {
    name: t("solvedProblems.solved"),
    data: [10, 20, 15, 30, 25, 40, 35],
  },
  {
    name: t("solvedProblems.pending"),
    data: [25, 49, 93, 54, 35, 64, 87],
  },
]);

const chartOptions = computed<ApexOptions>(() => ({
  chart: {
    type: "area",
    height: 200,
    toolbar: {
      show: false,
    },
  },
  colors: ["#4CAF50"],
  dataLabels: {
    enabled: false,
  },
  stroke: {
    curve: "smooth",
    width: 2,
  },
  xaxis: {
    categories: [
      t("months.jan"),
      t("months.feb"),
      t("months.mar"),
      t("months.apr"),
      t("months.may"),
      t("months.jun"),
      t("months.jul"),
    ],
    labels: {
      style: {
        colors: "#9CA3AF",
      },
    },
  },
  yaxis: {
    labels: {
      style: {
        colors: "#9CA3AF",
      },
    },
  },
  tooltip: {
    theme: "dark",
  },
}));
</script>

<template>
  <div class="overflow-hidden rounded-md border border-default sm:px-6 sm:pt-6">
    <div class="flex items-center justify-between">
      <div>
        <h3 class="text-lg font-semibold">
          {{ t("solvedProblems.title") }}
        </h3>
        <p class="mt-1 text-sm">
          {{ t("solvedProblems.subtitle") }}
        </p>
      </div>
      <USelectMenu
        v-model="selectedItem"
        :search-input="false"
        :items="items"
        class="w-48"
      />
    </div>

    <div class="mt-4">
      <VueApexCharts
        type="area"
        height="200"
        :options="chartOptions"
        :series="series"
      />
    </div>
  </div>
</template>
