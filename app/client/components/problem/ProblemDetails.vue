<script lang="ts" setup>
import { useProblemReports } from "~/composables/use-problem-reports";

interface Props {
  problemId: string;
}
const props = defineProps<Props>();

const emit = defineEmits<{
  (e: "close"): void;
}>();

const { reports, loading, error } = useProblemReports(props.problemId);

const toast = useToast();
if (error.value) {
  toast.add({
    title: "Error",
    description: error.value.message,
    color: "error",
  });
}
</script>

<template>
  <aside class="size-full flex flex-col p-4">
    <header class="flex items-center mb-3">
      <button
        class="cursor-pointer transition hover:scale-110"
        @click="emit('close')"
      >
        <Icon name="lucide:x" mode="svg" size="3rem" />
      </button>
    </header>
    <main class="h-full">
      <div v-if="loading">Loading...</div>
      <ProblemReport
        v-else
        v-for="report in reports"
        :report="report"
        :key="report.id"
      />
    </main>
  </aside>
</template>
