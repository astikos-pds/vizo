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
    <header class="h-[6%]">
      <button class="pointer" @click="emit('close')">
        <IconClose class="w-[50px] h-[50px]" />
      </button>
    </header>
    <main class="h-full">
      <div v-for="report in reportsOfProblem">
        <img :src="report.imageUrl" alt="Image" />
        <p>{{ report.description }}</p>
      </div>
    </main>
  </aside>
</template>
