<script lang="ts" setup>
import { ref, computed } from 'vue';
import VueApexCharts from 'vue3-apexcharts';
import { useI18n } from 'vue-i18n';
import type { ApexOptions } from 'apexcharts';
const { t, locale } = useI18n();

const options = computed(() => [
  { label: t('solvedProblems.today'), value: 'today' },
  { label: t('solvedProblems.week'), value: 'week' },
  { label: t('solvedProblems.month'), value: 'month' },
]);
const selected = ref('today');

const series = computed(() => [
  {
    name: t('solvedProblems.solved'),
    data: [10, 20, 15, 30, 25, 40, 35],
  },
  {
    name: t('solvedProblems.pending'),
    data: [25, 49, 93, 54, 35, 64, 87],
  },
]);

const chartOptions = computed<ApexOptions>(() => ({
  chart: {
    type: 'area',
    height: 200,
    toolbar: {
      show: false,
    },
  },
  colors: ['#4CAF50'],
  dataLabels: {
    enabled: false,
  },
  stroke: {
    curve: 'smooth',
    width: 2,
  },
  xaxis: {
    categories: [
      t('months.jan'),
      t('months.feb'),
      t('months.mar'),
      t('months.apr'),
      t('months.may'),
      t('months.jun'),
      t('months.jul'),
    ],
    labels: {
      style: {
        colors: '#9CA3AF',
      },
    },
  },
  yaxis: {
    labels: {
      style: {
        colors: '#9CA3AF',
      },
    },
  },
  tooltip: {
    theme: 'dark',
  },
}));
</script>

<template>
  <div
    class="rounded-2xl border border-gray-200 bg-white px-4 pb-4 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 sm:pt-6 sm:pb-6"
  >
    <div class="mb-6 flex flex-col gap-5 sm:flex-row sm:justify-between">
      <div class="w-full">
        <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">
          {{ t('solvedProblems.title') }}
        </h3>
        <p class="mt-1 text-sm text-gray-500 dark:text-gray-400">
          {{ t('solvedProblems.subtitle') }}
        </p>
      </div>
      <Dropdown :options="options" v-model="selected" />
    </div>
    <div class="custom-scrollbar max-w-full overflow-x-auto">
      <div class="min-w-[350px] sm:min-w-[600px] md:min-w-[800px] xl:min-w-full pl-0 sm:pl-2">
        <VueApexCharts
          type="area"
          height="200"
          :options="chartOptions"
          :series="series"
        />
      </div>
    </div>
  </div>
</template>