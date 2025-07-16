<script lang="ts" setup>
import { problemRepository } from "~/repositories/problem-repository";

const props = defineProps<{
  modelValue: string | string[];
  multiple?: boolean;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: string | string[]): void;
}>();

const value = computed({
  get: () => props.modelValue,
  set: (val: string | string[]) => emit("update:modelValue", val),
});

const { data: problemTypes, pending } =
  await problemRepository.getAllProblemTypes({
    key: "problem-types",
  });

const items = ref(problemTypes.value ?? []);
</script>

<template>
  <USelectMenu
    v-model="value"
    :loading="pending"
    :multiple="multiple"
    :items="items"
    placeholder="Select problem type(s)..."
  />
</template>
