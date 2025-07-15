<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import DepartmentAssignmentModal from "./DepartmentAssignmentModal.vue";
import type { Department } from "~/types/domain";

const department = defineProps<Department>();

const overlay = useOverlay();

function openModal() {
  const modal = overlay.create(DepartmentAssignmentModal);

  modal.open(department);
}

function deleteDepartment(id: string) {}

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: "Assign",
      icon: "i-lucide-user-plus",
      onSelect: openModal,
    },
  ],
  [
    {
      label: "Edit",
      icon: "i-lucide-pencil",
      to: `/municipality/${department.municipalityId}/departments/${department.id}/edit`,
    },
    {
      label: "Delete",
      icon: "i-lucide-trash",
      onSelect: (_) => deleteDepartment(department.id),
    },
  ],
]);
</script>

<template>
  <UDropdownMenu :items="items">
    <UButton
      icon="i-lucide-ellipsis-vertical"
      color="neutral"
      variant="ghost"
      size="xl"
    />
  </UDropdownMenu>
</template>
