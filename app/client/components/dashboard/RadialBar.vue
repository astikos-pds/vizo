<script setup lang="ts">
import { ref } from "vue";
import VueApexCharts from "vue3-apexcharts";
import type { ApexOptions } from "apexcharts";

const series = ref([67, 84, 97, 61]);

const chartOptions: ApexOptions = {
  chart: {
    height: 280,
    type: "radialBar",
  },
  plotOptions: {
    radialBar: {
      dataLabels: {
        total: {
          show: true,
          label: "TOTAL",
          formatter: function (w) {
            return w.globals.seriesTotals.reduce((a: number, b: number) => {
              return a + b;
            }, 0);
          },
        },
      },
    },
  },
  labels: ["TEAM A", "TEAM B", "TEAM C", "TEAM D"],
};
</script>

<template>
  <div style="width: 300px;">
    <VueApexCharts
      type="radialBar"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>
