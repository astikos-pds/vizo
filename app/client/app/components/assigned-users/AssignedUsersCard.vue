<script lang="ts" setup>
import type { AssignedUser } from "~/types/domain/assigned-user";

const { assignedUser } = defineProps<{
  assignedUser: AssignedUser;
  selected?: boolean;
}>();

const emit = defineEmits<{
  click: [a: AssignedUser];
}>();
</script>

<template>
  <UButton
    :color="!selected ? 'neutral' : 'primary'"
    :variant="!selected ? 'link' : 'subtle'"
    class="p-0 w-full text-start font-normal cursor-pointer border-b border-default rounded-none"
    :class="{ 'cursor-auto': selected }"
    @click="emit('click', assignedUser)"
  >
    <div class="size-full flex">
      <div class="h-full w-1" :class="{ 'bg-primary': selected }"></div>
      <AssignedUsersCardBody v-bind="assignedUser" class="p-3" />
    </div>
  </UButton>
</template>
