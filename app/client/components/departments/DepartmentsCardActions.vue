<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import DepartmentsAssignmentModal from "./DepartmentsAssignmentModal.vue";
import { useDepartments } from "~/composables/use-departments";
import type { Department } from "~/types/domain/department";

const department = defineProps<Department>();

const overlay = useOverlay();

function openModal() {
  const modal = overlay.create(DepartmentsAssignmentModal);

  modal.open(department);
}

const { deleteDepartment } = useDepartments();
const toast = useToast();

async function onDelete(department: Department) {
  const municipalityId = department.municipality.id;

  await deleteDepartment(municipalityId, department.id);

  toast.add({
    title: "Success",
    description: "Department deleted successfully!",
    color: "success",
  });

  await refreshNuxtData(`my-municipalities-${municipalityId}-assignments`);
}

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: "Assign users",
      icon: "i-lucide-user-plus",
      onSelect: openModal,
    },
    {
      label: "Edit",
      icon: "i-lucide-pencil",
      to: `/municipalities/${department.municipality.id}/departments/${department.id}/edit`,
    },
  ],
  [
    {
      label: "Delete",
      icon: "i-lucide-trash",
      color: "error",
      onSelect: () => onDelete(department),
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
