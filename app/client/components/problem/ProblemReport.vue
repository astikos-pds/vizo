<script lang="ts" setup>
import type { Report } from "~/types/domain/report";

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
      header: 'p-2 sm:px-2 2xl:p-2',
      body: 'p-0 sm:p-0',
      footer: 'p-2 sm:px-2 2xl:p-2 flex items-end justify-end',
    }"
  >
    <template #header
      ><p>{{ report.description }}</p></template
    >
    <section class="flex flex-col gap-3">
      <img
        v-for="(imageUrl, index) in report.imagesUrls"
        :key="`${report.id}-${index}`"
        :src="imageUrl.toString()"
        class="w-full h-full object-cover"
        :alt="`Image ${index} of report with id ${report.id}`"
      />
    </section>

    <template #footer
      >{{ t("problemReport.reportedAt") }}
      {{ report.createdAt.toLocaleDateString(locale) }}</template
    >
  </UCard>
</template>
