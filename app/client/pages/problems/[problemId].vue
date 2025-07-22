<script setup lang="ts">
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { computed } from "vue";
import { useDepartmentStore } from "~/stores/department";
import type { ProblemStatus } from "~/types/domain";
import type { BadgeProps } from "@nuxt/ui";
import { useProblems } from "~/composables/use-problems";

const { t } = useI18n();
const route = useRoute();

definePageMeta({
  name: "Problem",
  middleware: ["auth", "official", "department"],
});

const problemId = computed(() => route.params.problemId as string);
const { currentDepartment } = useDepartmentStore();

const municipalityId = computed(() => currentDepartment?.municipality.id ?? "");
const departmentId = computed(() => currentDepartment?.id ?? "");

const { getProblemInDepartmentContext } = useProblems();

const { data: problem } = await getProblemInDepartmentContext(
  municipalityId.value,
  departmentId.value,
  problemId.value
);

const { getReportsByProblemId } = useReports();

const { data: page } = await getReportsByProblemId(problemId.value);

const reports = computed(() => {
  if (page.value) {
    return page.value.content;
  }
});

const colorByStatus: Record<ProblemStatus, BadgeProps["color"]> = {
  ANALYSIS: "warning",
  IN_PROGRESS: "info",
  SOLVED: "success",
  REJECTED: "error",
};

const statusColor = computed(() => {
  if (problem.value) {
    return colorByStatus[problem.value.status];
  }
});
</script>

<template>
  <div
    v-if="problem"
    class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1"
  >
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-center">
      <div class="w-full max-w-2xl">
        <Map
          class="rounded-md border border-default"
          :zoom="16"
          :center="[problem.latitude, problem.longitude]"
          style="height: 250px; width: 100%"
        >
          <Marker
            :lat-lng="{
              latitude: problem.latitude,
              longitude: problem.longitude,
            }"
          />
        </Map>
      </div>

      <h2 class="text-2xl font-bold my-8">{{ t("details.title") }}</h2>

      <div class="w-full max-w-2xl space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="space-y-2">
            <div>
              <span class="font-semibold">{{ t("details.form.id") }}:</span>
              <span class="ml-2">{{ problem.id }}</span>
            </div>
            <div>
              <span class="font-semibold">{{ t("details.form.title") }}:</span>
              <span class="ml-2">{{ problem.type }}</span>
            </div>
            <div>
              <span class="font-semibold">{{ t("details.form.status") }}:</span>
              <UBadge
                :color="statusColor"
                variant="subtle"
                class="ml-2 capitalize"
              >
                {{ problem.status }}
              </UBadge>
            </div>
          </div>
        </div>

        <!-- Relatos -->
        <div
          v-if="reports"
          class="rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-white/[0.03] p-6"
        >
          <h3 class="text-xl font-semibold mb-6">
            {{ t("details.reports.title") }}
          </h3>

          <div class="flex flex-col gap-8">
            <div
              v-for="(report, idx) in reports"
              :key="report.id"
              class="flex flex-col gap-4"
            >
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div class="flex flex-col gap-2">
                  <div>
                    <span class="font-semibold"
                      >{{ t("details.reports.user") }}:</span
                    >
                    <span class="ml-1">{{ report.citizen.name }}</span>
                  </div>
                  <div>
                    <span class="font-semibold"
                      >{{ t("details.reports.date") }}:</span
                    >
                    <span class="ml-1">{{
                      new Date(report.createdAt).toLocaleString()
                    }}</span>
                  </div>
                </div>

                <div class="flex flex-col gap-2 md:col-span-2">
                  <div>
                    <span class="font-semibold">{{
                      t("details.reports.description")
                    }}</span>
                  </div>
                  <div
                    class="bg-white dark:bg-neutral-900 rounded-lg border border-gray-200 dark:border-gray-700 p-3 max-h-40 overflow-auto break-words"
                  >
                    {{ report.description }}
                  </div>

                  <div
                    v-if="report.images.length > 0"
                    class="flex gap-2 mt-2 flex-wrap"
                  >
                    <img
                      v-for="(img, i) in report.images"
                      :key="i"
                      :src="img.url"
                      class="rounded-lg w-[120px] h-[120px] object-cover aspect-square"
                      :alt="t('details.reports.imageAlt')"
                    />
                  </div>
                </div>
              </div>

              <hr
                v-if="idx < reports.length - 1"
                class="border-t border-gray-300 dark:border-gray-700 my-4"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
