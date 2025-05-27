<script lang="ts" setup>
import { reports } from "~/data";

interface Props {
  problemId: number;
}
const props = defineProps<Props>();

const reportsOfProblem = computed(() =>
  reports.filter((report) => props.problemId === report.problemId)
);

const emit = defineEmits<{
  (e: "close"): void;
}>();
</script>

<template>
  <aside class="flex flex-col p-3">
    <header class="h-[10%] flex items-center">
      <button
        class="cursor-pointer transition hover:scale-110"
        @click="emit('close')"
      >
        <Icon name="tabler:x" mode="svg" size="3rem" />
      </button>
    </header>
    <main class="h-full">
      <div v-for="report in reportsOfProblem">
        <p>{{ report.description }}</p>
        <img :src="report.imageUrl" class="w-full" alt="Image" />
      </div>
    </main>
  </aside>
</template>
