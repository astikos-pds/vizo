<script lang="ts" setup>
import type { Report } from "~/types/domain";

interface Props {
  report: Report;
  variant?: "outline" | "solid" | "soft" | "subtle";
}
const { report } = defineProps<Props>();
</script>

<template>
  <UCard
    :variant="variant ?? 'outline'"
    color="info"
    :ui="{
      header: 'p-2 sm:px-2.5 2xl:p-2.5 flex flex-row gap-1',
      body: 'p-0 sm:p-0',
      footer: 'p-2 sm:px-2.5 2xl:p-2.5 flex flex-col gap-1',
    }"
  >
    <template #header>
      <span class="font-semibold">Description:</span>
      <p>{{ report.description }}</p>
    </template>

    <section v-if="report.imagesUrls.length !== 0">
      <p class="p-2 font-semibold">Images:</p>
      <section class="border-t border-default flex flex-row flex-wrap gap-1">
        <div class="size-[5rem]" v-for="(imageUrl, index) in report.imagesUrls">
          <img
            class="w-full h-full object-cover"
            :src="imageUrl"
            :alt="`Image ${index}`"
            :key="index"
          />
        </div>
      </section>
    </section>

    <template #footer>
      <section class="flex flex-row gap-1">
        <span class="font-semibold">Coordinates:</span>
        <p>{{ report.latitude }}, {{ report.longitude }}</p>
      </section>
    </template>
  </UCard>
</template>
