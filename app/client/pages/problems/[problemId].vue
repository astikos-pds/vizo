<!-- <script setup lang="ts">
import { ref } from "vue";
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Problem, ProblemStatus, Report } from "~/types/domain";
import type { BadgeProps } from "@nuxt/ui";

const { t } = useI18n();
const route = useRoute();
const problemId = computed(() => route.params.problemId as string);

const { currentDepartment } = useDepartmentStore();
const municipalityId = computed(() => currentDepartment?.municipality.id ?? "");
const departmentId = computed(() => currentDepartment?.id ?? "");

// const { data: problems } = await municipalityRepository.getDepartmentProblemById(
//   municipalityId.value,
//   departmentId.value,
//   problemId.value,
//   {
//     key: `municipalities-${municipalityId.value}-departments-${departmentId.value}-problems-${problemId.value}`,
//     watch: [municipalityId, departmentId, problemId],
//   }
// );

const problem: Problem = {
  id: "",
  status: "REJECTED",
  latitude: -23.6045,
  longitude: -46.6694,
  type: "",
  accumulatedCredibility: 0,
  validated: false,
  firstReportedAt: "",
  lastReportedAt: "",
};

const reports: Report[] = [
  {
    id: "",
    description: "Buraco",
    images: [],
    latitude: -23.6045,
    longitude: -46.6694,
    citizen: {},
    problem: problem,
    createdAt: "2023-06-29",
  },
];

const colorByStatus: Record<ProblemStatus, BadgeProps["color"]> = {
  ANALYSIS: "warning",
  IN_PROGRESS: "info",
  SOLVED: "success",
  REJECTED: "error",
};

const color = computed(() => {
  return colorByStatus[problem.status];
});
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1">
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-center">
      <div class="w-full max-w-2xl mb-6">
        <Map
          class="rounded-xl border border-default mb-4"
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
      <h2 class="text-2xl font-bold mb-2">{{ t("details.title") }}</h2>
      <div class="w-full max-w-2xl">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div class="flex flex-col gap-4">
            <UForm :state="problem" class="flex flex-col gap-4">
              <UFormField :label="t('details.form.id')" name="id">
                <UInput v-model="problem.id" disabled />
              </UFormField>
              <UFormField :label="t('details.form.title')" name="title">
                <UInput v-model="problem.type" disabled />
              </UFormField>
              <UFormField :label="t('details.form.status')" name="status">
                <UBadge :color="color" variant="subtle">
                  {{ t("details.statusLabel." + problem.status) }}
                </UBadge>
              </UFormField>
              <UFormField :label="t('details.form.date')" name="date">
                <UInput v-model="problem.firstReportedAt" disabled />
              </UFormField>
              <UFormField :label="t('details.form.address')" name="address">
                <UTextarea
                  v-model="problem.id"
                  :rows="2"
                  autoresize
                  disabled
                  class="max-h-20 overflow-auto break-words"
                />
              </UFormField>
            </UForm>
          </div>
          <div class="flex flex-col gap-4">
            <UFormField
              :label="t('details.form.description')"
              name="description"
            >
              <UTextarea
                v-model="problem.type"
                :rows="4"
                autoresize
                disabled
                class="max-h-40 overflow-auto break-words"
              />
            </UFormField>
          </div>
        </div>
        <div
          class="rounded-xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] p-6 mt-8"
        >
          <h3 class="text-xl font-semibold mb-6">
            {{ t("details.reports.title") }}
          </h3>
          <div class="flex flex-col gap-8">
            <div
              v-for="(report, idx) in reports"
              :key="idx"
              class="flex flex-col gap-4"
            >
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div class="flex flex-col gap-2">
                  <div>
                    <span class="font-semibold">{{
                      t("details.reports.user")
                    }}</span>
                    {{ report.citizen.name }}
                  </div>
                  <div>
                    <span class="font-semibold">{{
                      t("details.reports.date")
                    }}</span>
                    {{ report.createdAt }}
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
                  <div class="w-[120px] h-[120px] mt-2">
                    <img
                      :src="report.images"
                      :alt="t('details.reports.imageAlt')"
                      class="rounded-lg w-full h-full object-cover aspect-square"
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
</template> -->

<script setup lang="ts">
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { computed } from "vue";
import { useDepartmentStore } from "~/stores/department";
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Problem, ProblemStatus, Report } from "~/types/domain";
import type { BadgeProps } from "@nuxt/ui";
import { problemRepository } from "~/repositories/problem-repository";

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

const { data: problem } = await municipalityRepository.getDepartmentProblemById(
  municipalityId.value,
  departmentId.value,
  problemId.value,
  {
    key: `municipalities-${municipalityId.value}-departments-${departmentId.value}-problems-${problemId.value}`,
    watch: [municipalityId, departmentId, problemId],
  }
);

const { data: page } = problemRepository.getReportsById(problemId.value, {
  watch: [problemId],
});
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
                {{ t("details.statusLabel." + problem.status) }}
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
