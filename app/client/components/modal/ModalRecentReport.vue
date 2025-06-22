<script lang="ts" setup>
import type { Report } from "~/types/domain";
import { REPORT_CONFLICT_PERIOD_IN_DAYS } from "~/utils/constants";

interface Props {
  lastReport: Report;
  currentReport: Report;
}
const { lastReport } = defineProps<Props>();

const emit = defineEmits<{ close: [boolean] }>();
</script>

<template>
  <UModal
    title="You probably already reported this problem"
    :description="`You reported a problem within ${RADIUS_OF_RELATED_REPORTS_IN_METERS} meters in the last ${
      REPORT_CONFLICT_PERIOD_IN_DAYS / 7
    } weeks. Are you sure you want to report this problem again?`"
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
          label="Cancel"
          class="cursor-pointer"
          @click="emit('close', false)"
        />
        <UButton
          color="warning"
          label="Send anyway"
          variant="subtle"
          class="cursor-pointer"
          @click="emit('close', true)"
        />
      </div>
    </template>
  </UModal>
</template>
