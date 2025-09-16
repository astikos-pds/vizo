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
  lastReport: PartialReport & { createdAt: Date };
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
      <section class="flex flex-col gap-3">
        <div class="w-full flex gap-3 items-center justify-center">
          <UCard class="w-full">
            <template #header>
              <span class="font-semibold">Last report</span>
            </template>

            <div class="size-full flex flex-col">
              <span>"{{ lastReport.description }}"</span>
              <span
                >Reported at
                {{ lastReport.createdAt.toLocaleDateString() }}</span
              >
            </div>
          </UCard>

          <Icon name="lucide:git-compare" class="text-4xl" />

          <UCard class="w-full">
            <template #header>
              <span class="font-semibold">Current report</span>
            </template>

            "{{ currentReport.description }}"
          </UCard>
        </div>
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
