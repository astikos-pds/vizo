<script lang="ts" setup>
import {
  REPORT_CONFLICT_PERIOD_IN_DAYS,
  RADIUS_OF_RELATED_REPORTS_IN_METERS,
} from "~/utils/constants";

interface PartialReport {
  latitude: number;
  longitude: number;
  description: string;
  imagesUrls: string[];
}

interface Props {
  lastReport: PartialReport;
  currentReport: PartialReport;
}
const { lastReport, currentReport } = defineProps<Props>();
const emit = defineEmits<{ close: [boolean] }>();

const { t } = useI18n();
</script>

<template>
  <UModal
    :title="t('conflictModal.title')"
    :description="
      t('conflictModal.description', {
        meters: RADIUS_OF_RELATED_REPORTS_IN_METERS,
        weeks: REPORT_CONFLICT_PERIOD_IN_DAYS / 7,
      })
    "
    :ui="{
      body: 'p-5 sm:p-5',
    }"
  >
    <template #body>
      <section
        class="flex flex-col md:flex-row gap-3 items-center justify-center"
      >
        <ReportCard :report="currentReport" />
        <Icon name="lucide:git-compare" class="text-4xl" />
        <ReportCard :report="lastReport" variant="soft" class="opacity-85" />
      </section>
    </template>
    <template #footer>
      <div class="flex gap-2">
        <UButton
          color="neutral"
          variant="subtle"
          :label="t('common.cancel')"
          @click="emit('close', false)"
        />
        <UButton
          color="warning"
          variant="subtle"
          :label="t('conflictModal.confirm')"
          @click="emit('close', true)"
        />
      </div>
    </template>
  </UModal>
</template>
