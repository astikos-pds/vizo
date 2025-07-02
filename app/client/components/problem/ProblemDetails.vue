<script lang="ts" setup>
import { useProblemReports } from "~/composables/use-problem-reports";
import type { Problem } from "~/types/domain";
import type { Pageable } from "~/types/http";

const { t } = useI18n();

interface Props {
  problem: Problem;
}
const props = defineProps<Props>();

const pagination = reactive<Pageable>({
  page: 0,
  size: 2,
});

const currentPage = computed({
  get: () => (pagination.page ?? 0) + 1,
  set: (val: number) => (pagination.page = val - 1),
});

const { reports, loading, error } = useProblemReports(
  props.problem.id,
  pagination
);

const toast = useToast();
watch(error, (err) => {
  if (err) {
    toast.add({
      title: "Error",
      description: err.message,
      color: "error",
    });
  }
});

interface Badge {
  color:
    | "error"
    | "primary"
    | "secondary"
    | "success"
    | "info"
    | "warning"
    | "neutral"
    | undefined;
  text: string;
}

const statusBadge: Badge = (() => {
  if (props.problem.status === "ANALYSIS") {
    return {
      color: "warning",
      text: t("problemDetails.status.analysis"),
    };
  } else if (props.problem.status === "IN_PROGRESS") {
    return {
      color: "info",
      text: t("problemDetails.status.inProgress"),
    };
  } else if (props.problem.status === "SOLVED") {
    return {
      color: "warning",
      text: t("problemDetails.status.solved"),
    };
  }
  return {
    color: "neutral",
    text: t("problemDetails.status.default"),
  };
})();

const credibilityBadge: Badge = (() => {
  if (props.problem.accumulatedCredibility < 50) {
    return {
      color: "warning",
      text: t("problemDetails.credibility.low"),
    };
  } else if (props.problem.accumulatedCredibility >= 100) {
    return {
      color: "success",
      text: t("problemDetails.credibility.high"),
    };
  }
  return {
    color: "success",
    text: t("problemDetails.credibility.medium"),
  };
})();

const isDesktop = useMediaQuery("(min-width: 1024px)");
</script>

<template>
  <USlideover
    :direction="isDesktop ? 'right' : 'bottom'"
    :overlay="false"
    title="Problem details"
    :close="{
      size: 'xl',
    }"
    :ui="{
      footer: 'flex justify-center items-center',
    }"
    class="h-[40%] lg:w-80 lg:h-full"
  >
    <template #body>
      <main class="flex flex-row flex-wrap gap-4 h-full overflow-hidden">
        <div v-if="loading">Loading...</div>
        <section v-else class="flex flex-col gap-3 size-full">
          <section class="flex flex-row flex-wrap gap-2">
            <UBadge :color="statusBadge.color" variant="subtle">{{
              statusBadge.text
            }}</UBadge>
            <UBadge :color="credibilityBadge.color" variant="subtle">{{
              credibilityBadge.text
            }}</UBadge>
          </section>
          <USeparator />
          <section
            v-if="reports"
            class="p-1 pb-10 flex flex-row flex-wrap xl:flex-col xl:flex-nowrap gap-3 overflow-y-auto"
          >
            <ProblemReport
              v-for="report in reports.content"
              :report="report"
              :key="report.id"
              class="w-full sm:w-[49%] lg:w-full"
            />
          </section>
        </section>
      </main>
    </template>
    <template #footer>
      <UPagination
        v-model:page="currentPage"
        :items-per-page="pagination.size"
        :total="reports?.totalElements ?? 0"
      />
    </template>
  </USlideover>
</template>
