<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import DepartmentsAssignmentModal from "./DepartmentsAssignmentModal.vue";
import { useDepartments } from "~/composables/use-departments";
import type { Department } from "~/types/domain/department";

const { t } = useI18n();

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
    title: t('components.departments.success'),
    description: t('components.departments.departmentDeletedSuccessfully'),
    color: "success",
  });

  await refreshNuxtData(`my-municipalities-${municipalityId}-assignments`);
}

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: t('components.departments.assignUsers'),
      icon: "i-lucide-user-plus",
      onSelect: openModal,
    },
    {
      label: t('components.departments.edit'),
      icon: "i-lucide-pencil",
      to: `/municipalities/${department.municipality.id}/departments/${department.id}/edit`,
    },
  ],
  [
    {
      label: t('components.departments.delete'),
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
