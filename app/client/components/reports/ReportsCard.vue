<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { ProblemStatus, ProblemType } from "~/types/domain/problem";
import type { Report } from "~/types/domain/report";
import type { Badge } from "~/types/ui";

const { t, locale } = useI18n();

const report = defineProps<Report>();

const emit = defineEmits(["zoomIn"]);

const problemTypeSymbol: Record<ProblemType, { icon: string; color: string }> =
  {
    FLOODING: {
      icon: "waves",
      color: "#2bb0ed",
    },
    POTHOLE: {
      icon: "construction",
      color: "#ed9200",
    },
    POOR_LIGHTING: {
      icon: "lightbulb-off",
      color: "#edda09",
    },
    GARBAGE: {
      icon: "trash",
      color: "#1c1c1c",
    },
    OTHER: {
      icon: "circle-question-mark",
      color: "#919191",
    },
  };

const color = problemTypeSymbol[report.problem.type].color;
const icon = problemTypeSymbol[report.problem.type].icon;

const problemStatusToColor: Record<
  ProblemStatus,
  { color: Badge["color"]; icon: string }
> = {
  ANALYSIS: {
    color: "warning",
    icon: "i-lucide-chart-bar",
  },
  IN_PROGRESS: {
    color: "info",
    icon: "i-lucide-circle-dot",
  },
  RESOLVED: {
    color: "success",
    icon: "i-lucide-star",
  },
  REJECTED: {
    color: "error",
    icon: "i-lucide-message-square-x",
  },
};

const problemWasResolved = report.problem.status === "RESOLVED";

const { deleteReport } = useReports();

const onDelete = async (report: Report) => {
  await deleteReport(report.id);

  await refreshNuxtData();
};

const actions = computed<DropdownMenuItem[]>(() => {
  const base = [
    {
      label: t('components.reports.viewOnMap'),
      icon: "i-lucide-eye",
      onSelect: () => emit("zoomIn"),
    },
  ];

  if (report.problem.status !== "ANALYSIS") {
    return base;
  }

  return [
    ...base,
    {
      label: t('components.reports.edit'),
      icon: "i-lucide-pencil",
      to: `/reports/${report.id}/edit`,
    },
    {
      label: t('components.reports.delete'),
      icon: "i-lucide-trash",
      color: "error",
      onSelect: () => onDelete(report),
    },
  ];
});
</script>

<template>
  <UCard :variant="problemWasResolved ? 'soft' : 'outline'">
    <div class="size-full flex gap-3">
      <aside class="min-h-full">
        <div
          class="rounded-full bg-primary size-13 flex justify-center items-center text-neutral-50"
          :style="{ backgroundColor: color }"
        >
          <UIcon :name="`lucide:${icon}`" size="25" />
        </div>
      </aside>
      <main class="flex-1 flex flex-col">
        <span class="font-semibold">{{ report.problem.type }}</span>
        <span class="text-sm">"{{ report.description }}"</span>

        <UCollapsible v-if="report.imagesUrls.length > 0">
          <UButton
            :label="t('components.report.images')"
            color="neutral"
            :variant="problemWasResolved ? 'subtle' : 'outline'"
            trailing-icon="i-lucide-chevron-down"
            class="my-2"
            block
          />

          <template #content>
            <div class="w-full flex gap-1 py-2">
              <NuxtImg
                v-for="url in report.imagesUrls"
                :src="url.toString()"
                :key="url.username"
                :alt="url.username"
                class="border border-default"
              />
            </div>
          </template>
        </UCollapsible>

        <span class="text-sm"
          >{{ t('components.problem.reportedAt') }}
          {{
            report.createdAt.toLocaleDateString(locale, { dateStyle: "full" })
          }}</span
        >

        <div class="flex gap-1 items-center flex-wrap mt-2">
          <UBadge
            :color="problemStatusToColor[report.problem.status].color"
            :icon="problemStatusToColor[report.problem.status].icon"
            variant="soft"
            >{{ report.problem.status }}</UBadge
          >

          <UBadge
            v-if="report.problem.accumulatedCredibility >= 150"
            color="info"
            variant="soft"
            icon="i-lucide-users"
            >{{ t('components.reports.thisProblemIsBeingReported') }}</UBadge
          >

          <UBadge
            v-if="report.credibility >= 50"
            color="success"
            variant="soft"
            icon="i-lucide-circle-check"
            >{{ t('components.reports.thisReportWasVeryCredible') }}</UBadge
          >
        </div>
      </main>
      <div class="flex justify-center items-center">
        <UDropdownMenu :items="actions">
          <UButton
            variant="ghost"
            color="neutral"
            icon="i-lucide-ellipsis"
            size="xl"
          />
        </UDropdownMenu>
      </div>
    </div>

    <template #footer v-if="problemWasResolved && report.problem.resolvedAt">
      <span v-if="report.problem.resolvedAt" class="text-sm"
        >{{ t('components.reports.resolvedAt') }}
        {{
          report.problem.resolvedAt.toLocaleDateString(locale, {
            dateStyle: "full",
          })
        }}</span
      >
    </template>
  </UCard>
</template>
