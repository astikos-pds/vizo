<script setup lang="ts">
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { computed } from "vue";
import { useDepartmentStore } from "~/stores/department";
import type { BadgeProps } from "@nuxt/ui";
import { useProblems } from "~/composables/use-problems";
import type { ProblemStatus } from "~/types/domain/problem";
import ReportForProblem from "~/components/problem/ReportForProblem.vue";
import ModalChangeProblemStatus from "~/components/modal/ModalChangeProblemStatus.vue";

const { t, locale } = useI18n();
const route = useRoute();

useHead({
  title: t("head.problem.title"),
  meta: [
    {
      name: "description",
      content: t("head.problem.description"),
    },
  ],
});

definePageMeta({
  name: "Problem",
  middleware: ["auth", "assigned"],
});

const problemId = computed(() => route.params.problemId as string);
const { currentAssignment } = useLoggedInUserStore();

const municipalityId = computed(
  () => currentAssignment?.department.municipality.id ?? ""
);
const departmentId = computed(() => currentAssignment?.department.id ?? "");

const { getProblemInScope } = useDepartments();

const { data: problem } = await getProblemInScope(
  municipalityId.value,
  departmentId.value,
  problemId.value
);

const { getReportsForProblem } = useProblems();

const { data: page } = await getReportsForProblem(problemId.value);

const reports = computed(() => {
  if (page.value) {
    return page.value.content;
  }
});

const colorByStatus: Record<ProblemStatus, BadgeProps["color"]> = {
  ANALYSIS: "warning",
  IN_PROGRESS: "info",
  RESOLVED: "success",
  REJECTED: "error",
};

const statusColor = computed(() => {
  if (problem.value) {
    return colorByStatus[problem.value.status];
  }
});

const overlay = useOverlay();

function openModal() {
  if (!problem.value) return;

  const modal = overlay.create(ModalChangeProblemStatus, {
    props: {
      problem: problem.value,
    },
  });

  modal.open();
}
</script>

<template>
  <div
    v-if="problem"
    class="size-full flex justify-center gap-6 p-4 sm:p-6 md:p-8 overflow-auto"
  >
    <div class="rounded-2xl flex flex-col items-center gap-5 xl:w-[60%]">
      <div class="w-full">
        <Map
          class="w-full min-h-80 rounded-md border border-default"
          :zoom="16"
          :center="problem"
        >
          <Marker
            :lat-lng="{
              latitude: problem.latitude,
              longitude: problem.longitude,
            }"
          />
        </Map>
      </div>

      <div class="w-full">
        <div class="flex flex-col gap-2">
          <div class="flex gap-1.5">
<<<<<<< HEAD
            <span class="font-semibold">{{ t("pages.problem.type") }}</span>
=======
            <span class="font-semibold">{{ t('components.problem.type') }}:</span>
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
            <UBadge color="neutral" variant="subtle" size="lg">{{
              problem.type
            }}</UBadge>
          </div>
          <div class="flex gap-1.5">
            <span class="font-semibold">{{ t('components.problem.status') }}:</span>
            <UButtonGroup size="lg">
              <UBadge :color="statusColor" variant="subtle">
                {{ problem.status }}
              </UBadge>
              <UButton
                v-if="currentAssignment?.effectivePermission.canUpdateStatus"
                color="neutral"
                variant="outline"
                icon="i-lucide-pencil"
                @click="openModal"
              />
            </UButtonGroup>
          </div>
          <div class="flex gap-1.5">
<<<<<<< HEAD
            <span class="font-semibold">{{
              t("pages.problem.firstReportedAt")
            }}</span>
=======
            <span class="font-semibold">{{ t('components.problem.firstReportedAt') }}:</span>
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
            <span>{{
              problem.firstReportedAt.toLocaleDateString(locale, {
                dateStyle: "full",
              })
            }}</span>
          </div>

          <div class="flex gap-1.5">
<<<<<<< HEAD
            <span class="font-semibold">{{
              t("pages.problem.lastReportedAt")
            }}</span>
=======
            <span class="font-semibold">{{ t('components.problem.lastReportedAt') }}:</span>
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
            <span>{{
              problem.lastReportedAt.toLocaleDateString(locale, {
                dateStyle: "full",
              })
            }}</span>
          </div>

          <div class="flex gap-1.5">
<<<<<<< HEAD
            <span class="font-semibold">{{
              t("pages.problem.credibility")
            }}</span>
=======
            <span class="font-semibold">{{ t('components.problem.credibility') }}:</span>
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
            <span>{{ problem.accumulatedCredibility.toFixed(0) }}</span>
          </div>
        </div>
      </div>

      <div
        v-if="reports && currentAssignment?.effectivePermission.canViewReports"
        class="w-full rounded-md border border-default p-4"
      >
        <h3 class="text-xl font-semibold mb-6">
          {{ t('components.problem.userReports') }}
        </h3>

        <div class="flex flex-col gap-5">
          <ReportForProblem
            v-for="report in reports"
            :key="report.id"
            :report="report"
          />
        </div>
      </div>
    </div>
  </div>
</template>
