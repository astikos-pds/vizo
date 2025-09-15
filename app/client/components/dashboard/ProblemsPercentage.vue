<script setup lang="ts">
import { ref } from "vue";
import VueApexCharts from "vue3-apexcharts";
import type { ApexOptions } from "apexcharts";
import Filter from "./Filter.vue";

const { t } = useI18n();

const series = ref([44, 55, 41]);

const chartOptions: ApexOptions = {
  chart: {
    type: "donut",
    height: 280,
    toolbar: { show: false },
  },
  colors: ["#D3F527", "#F5B027", "#F54927"],
  labels: ["Pendente", "Em Andamento", "Concluído"],
  legend: {
    position: "right",
    fontSize: "14px",
    fontWeight: 500,
    labels: { colors: "#333" },
    // markers: { width: 12, height: 12, radius: 6 },
  },
  dataLabels: {
    enabled: false,
  },
  plotOptions: {
    pie: {
      donut: {
        labels: {
          show: false,
          total: {
            show: true,
            label: "Total",
            fontSize: "16px",
            fontWeight: 600,
            color: "#333",
          },
        },
      },
    },
  },
  responsive: [
    {
      breakpoint: 480,
      options: {
        chart: { width: 200 },
        legend: { position: "bottom" },
      },
    },
  ],
};
</script>

<template>
  <div class="border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] p-6 rounded-lg w-full xl:w-[30%] self-start max-h-[500px] overflow-auto">
    <h3 class="text-center mb-4">Situação Atual dos Problemas</h3>
    <div class="flex justify-end">
      <Filter/>
    </div>
    <VueApexCharts
      type="donut"
      :options="chartOptions"
      :series="series"
      height="280"
    />
  </div>
</template>
