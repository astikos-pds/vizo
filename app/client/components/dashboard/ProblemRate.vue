<script setup lang="ts">
import { ref, computed } from "vue";
import VueApexCharts from "vue3-apexcharts";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

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

const problems = ref([
  {
    label: "Buracos na Rua",
    count: 168,
  },
  {
    label: "Árvore Caída",
    count: 385,
  },
  {
    label: "Lixo na Rua",
    count: 201,
  },
  {
    label: "Iluminação Pública",
    count: 298,
  }
]);

const selectedItem = ref(items.value[0]);

const series = computed(() => [
  {
    name: t("problemRate.incidence"),
    data: problems.value.map(p => p.count),
  },
]);

const chartOptions = computed(() => ({
  colors: ["#465fff"],
  chart: {
    fontFamily: "Outfit, sans-serif",
    type: "bar",
    toolbar: {
      show: false,
    },
  },
  plotOptions: {
    bar: {
      horizontal: false,
      columnWidth: "39%",
      borderRadius: 5,
      borderRadiusApplication: "end",
    },
  },
  dataLabels: {
    enabled: false,
  },
  stroke: {
    show: true,
    width: 4,
    colors: ["transparent"],
  },
  xaxis: {
    categories: problems.value.map(p => p.label),
    axisBorder: {
      show: false,
    },
    axisTicks: {
      show: false,
    },
  },
  legend: {
    show: true,
    position: "top",
    horizontalAlign: "left",
    fontFamily: "Outfit",
    markers: {
      radius: 99,
    },
  },
  yaxis: {
    title: false,
    tickAmount: 4,
  },
  grid: {
    yaxis: {
      lines: {
        show: true,
      },
    },
  },
  fill: {
    opacity: 1,
  },
  tooltip: {
    x: {
      show: false,
    },
    y: {
      formatter: function (val: number) {
        return val.toString();
      },
    },
  },
}));
</script>

<template>
  <div
    class="overflow-hidden rounded-md border border-gray-200 bg-white px-5 pt-5 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6"
  >
    <div class="flex items-center justify-between">
      <div>
        <h3 class="text-lg font-semibold">
          {{ t("problemRate.title") }}
        </h3>
        <p class="mt-1 text-sm text-muted">
          {{ t("problemRate.subtitle") }}
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
        type="bar"
        height="200"
        :options="chartOptions"
        :series="series"
      />
    </div>
  </div>
</template>
