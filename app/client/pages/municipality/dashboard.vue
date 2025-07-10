<script lang="ts" setup>

import ProblemRate from '~/components/dashboard/ProblemRate.vue'
import LastProblems from '~/components/dashboard/LastProblems.vue'
import SolvedProblems from '~/components/dashboard/SolvedProblems.vue'

const { t } = useI18n()

useHead({
  meta: [{ name: "viewport", content: "width=device-width, initial-scale=1.0" }],
});

definePageMeta({
  layout: 'admin',
})

</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-2">
    <ProblemRate class="col-span-1" />
    <SolvedProblems class="col-span-1" />
    <LastProblems class="col-span-1 md:col-span-2 xl:col-span-2" />
  </div>
</template>