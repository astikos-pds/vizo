<script lang="ts" setup>
import type { SelectMenuItem } from "@nuxt/ui";
import type { ProblemType } from "~/types/domain/problem";

const { multiple } = defineProps<{
  multiple?: boolean;
}>();

const model = defineModel<ProblemType | ProblemType[]>();

const { getProblemTypes } = useProblems();

const { data: problemTypes, pending } = await getProblemTypes();

const items = computed<SelectMenuItem[]>(() => {
  if (!problemTypes.value) return [];

  return [...problemTypes.value];
});
</script>

<template>
  <USelectMenu
    v-model="model"
    :loading="pending"
    :multiple="multiple"
    :items="items"
    icon="i-lucide-flag"
    placeholder="Select a problem type"
  />
</template>
