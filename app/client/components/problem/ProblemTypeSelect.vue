<script lang="ts" setup>
import type { ProblemType } from "~/types/domain";

defineProps<{
  multiple?: boolean;
}>();

const model = defineModel<ProblemType | ProblemType[]>();

const { getProblemTypes } = useProblems();

const { data: problemTypes, pending } = await getProblemTypes();

const items = ref(problemTypes.value ?? []);
</script>

<template>
  <USelectMenu
    v-model="model"
    :loading="pending"
    :multiple="multiple"
    :items="items"
    placeholder="AAAAAAAAAAAAAAAAA"
  />
</template>
