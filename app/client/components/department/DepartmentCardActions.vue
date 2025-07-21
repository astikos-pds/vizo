<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import DepartmentAssignmentModal from "./DepartmentAssignmentModal.vue";
import type { Department } from "~/types/domain";
import { municipalityRepository } from "~/repositories/municipality-repository";
import { useDepartments } from "~/composables/use-departments";

const department = defineProps<Department>();

const overlay = useOverlay();

function openModal() {
  const modal = overlay.create(DepartmentAssignmentModal);

  modal.open(department);
}

const toast = useToast();

async function deleteDepartment() {
  const municipalityId = department.municipality.id;

  await useDepartments().deleteDepartment(municipalityId, department.id);

  toast.add({
    title: "Success",
    description: "Department deleted successfully!",
    color: "success",
  });

  await refreshNuxtData(`municipalities-${municipalityId}-assignments`);
}

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: "Assign",
      icon: "i-lucide-user-plus",
      color: "info",
      onSelect: openModal,
    },
  ],
  [
    {
      label: "Edit",
      icon: "i-lucide-pencil",
      to: `/municipality/${department.municipality.id}/departments/${department.id}/edit`,
    },
    {
      label: "Delete",
      icon: "i-lucide-trash",
      color: "error",
      onSelect: deleteDepartment,
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
