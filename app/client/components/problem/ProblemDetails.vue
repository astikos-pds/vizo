<script lang="ts" setup>
import { useProblemReports } from "~/composables/use-problem-reports";
import type { Problem } from "~/types/domain";
import type { Pageable } from "~/types/http";

interface Props {
  problem: Problem;
}
const { problem } = defineProps<Props>();

const { t } = useI18n();

const pagination = reactive<Pageable>({
  page: 0,
  size: 15,
});

const currentPage = computed({
  get: () => (pagination.page ?? 0) + 1,
  set: (val: number) => (pagination.page = val - 1),
});

const { reports, loading, error } = useProblemReports(problem.id, pagination);

const toast = useToast();
watch(error, (err) => {
  if (err) {
    toast.add({
      title: t("toast.error.title"),
      description: t(
        `toast.error.description.${err.status}`,
        t("toast.error.description.default")
      ),
      color: "error",
    });
  }
});

const breakpoints = useBreakpoints({
  lg: 1024,
});

const isMobile = breakpoints.smallerOrEqual("lg");

const snapPoints = [0.3, 0.5, 0.9];
const activeSnapPoint = ref(snapPoints[1]);

const title = "Problem details";

const ui = {
  footer: "flex justify-center items-center",
};
</script>

<template>
  <UDrawer
    v-if="isMobile"
    :title="title"
    direction="bottom"
    :overlay="false"
    :ui="ui"
    :snap-points="snapPoints"
    :active-snap-point="activeSnapPoint"
    @update:active-snap-point="(value: number) => (activeSnapPoint = value as number)"
  >
    <template #body>
      <div v-if="loading" class="flex flex-col gap-4">
        <USkeleton class="h-50 w-full" />
        <USkeleton class="h-50 w-full" />
        <USkeleton class="h-50 w-full" />
      </div>
      <ProblemDetailsBody
        v-else
        v-if="reports"
        :problem="problem"
        :reports="reports"
      />
    </template>
    <template #footer>
      <UPagination
        v-model:page="currentPage"
        :items-per-page="pagination.size"
        :total="reports?.totalElements ?? 0"
      />
    </template>
  </UDrawer>

  <USlideover
    v-else
    side="right"
    :overlay="false"
    :title="title"
    :close="{
      class: 'text-xl',
    }"
    :ui="ui"
    class="w-100"
  >
    <template #body>
      <div v-if="loading" class="flex flex-col gap-4">
        <USkeleton class="h-50 w-full" />
        <USkeleton class="h-50 w-full" />
        <USkeleton class="h-50 w-full" />
      </div>
      <ProblemDetailsBody
        v-else
        v-if="reports"
        :problem="problem"
        :reports="reports"
      />
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
