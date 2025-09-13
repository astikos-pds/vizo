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
    color="neutral"
    :variant="!selected ? 'link' : 'outline'"
    class="p-0 w-full text-start font-normal cursor-pointer"
    :class="{ 'cursor-auto': selected }"
    @click="emit('click', assignedUser)"
  >
    <UCard
      color="neutral"
      variant="outline"
      class="size-full flex"
      :ui="{ body: 'p-0 sm:p-0' }"
    >
      <div class="size-full flex">
        <div class="h-full w-1" :class="{ 'bg-primary': selected }"></div>
        <AssignedUsersCardBody v-bind="assignedUser" class="p-3" />
      </div>
    </UCard>
  </UButton>
</template>
