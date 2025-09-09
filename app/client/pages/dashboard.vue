<script lang="ts" setup>
import type { Pageable } from "~/types/http";
import RadialBar from "~/components/dashboard/RadialBar.vue";
import ProblemPercentage from "~/components/dashboard/ProblemsPercentage.vue";
import ProblemRate from "~/components/dashboard/ProblemRate.vue";
import InfoCard from "~/components/dashboard/InfoCard.vue";

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

//definePageMeta({
//  name: "Dashboard",
//  middleware: ["auth", "official", "department"],
//});

const pagination = ref<Pageable>({
  page: 0,
  size: 5,
});
</script>

<template>
  <div class="grid grid-cols-4">
    <InfoCard value="765" label="Problemas Resolvidos" desc="nos últimos 30 dias" color="bg-gradient-to-tr from-green-600 to-green-300" />
    <InfoCard value="23" label="Problemas a Resolver" desc="" color="bg-gradient-to-tr from-yellow-500 to-yellow-300" />
  </div>

  <div
    class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-2"
  >
    <ProblemRate class="col-span-1 md:col-span-2 xl:col-span-1"/>
    <ProblemPercentage />
    <ProblemTable
      v-model:pagination="pagination"
      class="col-span-1 md:col-span-2 xl:col-span-2"
    />
    
  </div>  
</template>