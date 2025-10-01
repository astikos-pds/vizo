<script lang="ts" setup>
import type { Page } from "~/types/domain/pagination";
import type { Problem } from "~/types/domain/problem";
import type { Report } from "~/types/domain/report";
import type { Badge } from "~/types/ui";
import ReportForProblem from "./ReportForProblem.vue";

const { problem, reports } = defineProps<{
  problem: Problem;
  reports: Page<Report>;
}>();

const { t } = useI18n();

const statusBadge: Badge = (() => {
  if (problem.status === "ANALYSIS") {
    return {
      color: "warning",
      text: t("problemDetails.status.analysis"),
    };
  } else if (problem.status === "IN_PROGRESS") {
    return {
      color: "info",
      text: t("problemDetails.status.inProgress"),
    };
  } else if (problem.status === "RESOLVED") {
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
  if (problem.accumulatedCredibility < 50) {
    return {
      color: "warning",
      text: t("problemDetails.credibility.low"),
    };
  } else if (problem.accumulatedCredibility >= 100) {
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
</script>

<template>
  <main class="flex flex-row flex-wrap gap-4 overflow-hidden">
    <section class="flex flex-col gap-3 size-full">
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
        class="min-h-screen p-1 flex flex-col gap-3 overflow-y-auto"
      >
        <ReportForProblem
          v-for="report in reports.content"
          :report="report"
          :key="report.id"
          class="w-full"
        />
      </section>
    </section>
  </main>
</template>
