<script lang="ts" setup>
import { useProblemReports } from "~/composables/use-problem-reports";
import type { Problem } from "~/types/domain";

const { t } = useI18n();

interface Props {
  problem: Problem;
  isOpen: boolean;
}
const props = defineProps<Props>();

const emit = defineEmits<{
  (e: "close"): void;
}>();

const { reports, loading, error } = useProblemReports(props.problem.id);

const toast = useToast();
if (error.value) {
  toast.add({
    title: "Error",
    description: error.value.message,
    color: "error",
  });
}

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
    :dismissible="false"
    v-model:open="props.isOpen"
  >
    <template #content>
      <aside class="size-full flex flex-col p-2 xl:p-3">
        <header class="flex items-center mb-1 xl:mb-2">
          <UButton
            color="neutral"
            variant="ghost"
            icon="i-lucide-x"
            @click="emit('close')"
            class="text-2xl cursor-pointer"
          />
        </header>
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
              class="p-1 pb-10 flex flex-row flex-wrap xl:flex-col xl:flex-nowrap gap-3 overflow-y-auto"
            >
              <ProblemReport
                v-for="report in reports"
                :report="report"
                :key="report.id"
                class="w-full sm:w-[49%] lg:w-full"
              />
            </section>
          </section>
        </main>
      </aside>
    </template>
  </USlideover>
</template>
