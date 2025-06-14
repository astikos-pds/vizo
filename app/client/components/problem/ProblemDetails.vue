<script lang="ts" setup>
import { useProblemReports } from "~/composables/use-problem-reports";
import type { Problem } from "~/types/domain";

const { t } = useI18n();

interface Props {
  problem: Problem;
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

const statusBadge = computed<Badge>(() => {
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
});
const credibilityBadge = computed<Badge>(() => {
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
});
</script>

<template>
  <aside class="h-full w-full flex flex-col p-2 xl:p-3">
    <header class="flex items-center xl:mb-2">
      <button
        class="cursor-pointer transition hover:scale-110"
        @click="emit('close')"
      >
        <Icon
          name="lucide:x"
          mode="svg"
          class="text-3xl lg:text-[2.8rem] xl:text-[3rem]"
        />
      </button>
    </header>
    <main class="flex flex-row flex-wrap gap-4 h-full w-full">
      <div v-if="loading">Loading...</div>
      <section v-else class="flex flex-col gap-3 w-full p-1 overflow-y-scroll">
        <section class="flex flex-row flex-wrap gap-2 w-full">
          <UBadge :color="statusBadge.color" variant="subtle" size="lg">{{
            statusBadge.text
          }}</UBadge>
          <UBadge :color="credibilityBadge.color" variant="subtle" size="lg">{{
            credibilityBadge.text
          }}</UBadge>
        </section>
        <USeparator />
        <ProblemReport
          v-for="report in reports"
          :report="report"
          :key="report.id"
        />
      </section>
    </main>
  </aside>
</template>
