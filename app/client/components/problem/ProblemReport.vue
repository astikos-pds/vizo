<script lang="ts" setup>
import type { Report } from "~/types/domain";

interface Props {
  report: Report;
}
const { report } = defineProps<Props>();

const { locale, t } = useI18n();
</script>

<template>
  <UCard
    class="w-full md:w-[48%] lg:w-[31%] xl:min-w-full max-h-30%"
    variant="subtle"
    :ui="{
      header: 'p-2 sm:px-3 2xl:p-3',
      body: 'p-0 sm:p-0',
      footer: 'p-2 sm:px-3 2xl:p-3 flex items-end justify-end',
    }"
  >
    <template #header
      ><p>{{ report.description }}</p></template
    >
    <section class="flex flex-col gap-3 xl:max-h-[30rem]">
      <img
        v-for="(imageUrl, index) in report.imagesUrls"
        :key="`${report.id}-${index}`"
        :src="imageUrl"
        class="w-full h-full object-cover"
        :alt="`Image ${index} of report with id ${report.id}`"
      />
    </section>

    <template #footer
      >{{ t("problemReport.reportedAt") }}
      {{ new Date(report.createdAt).toLocaleDateString(locale) }}</template
    >
  </UCard>
</template>
