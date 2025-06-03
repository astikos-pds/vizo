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
    variant="subtle"
    :ui="{
      header: 'p-3',
      body: 'p-0 sm:p-0',
      footer: 'p-3 flex items-end justify-end',
    }"
  >
    <template #header
      ><p>{{ report.description }}</p></template
    >
    <section class="flex flex-col gap-3 overflow-hidden max-h-[30rem]">
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
