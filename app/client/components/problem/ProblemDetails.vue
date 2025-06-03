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
    <main
      class="flex items-center flex-row flex-wrap p-1 gap-4 h-full w-full overflow-y-scroll"
    >
      <div v-if="loading">Loading...</div>
      <ProblemReport
        v-else
        v-for="report in reports"
        :report="report"
        :key="report.id"
      />
      <ProblemReport
        v-for="report in reports"
        :report="report"
        :key="report.id"
      />
      <ProblemReport
        v-for="report in reports"
        :report="report"
        :key="report.id"
      />
    </main>
  </aside>
</template>
