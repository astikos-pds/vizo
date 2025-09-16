<script lang="ts" setup>
import DepartmentsForm from "~/components/departments/DepartmentsForm.vue";
import { useAssignedUsers } from "~/composables/use-assigned-users";
import { useDepartments } from "~/composables/use-departments";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Department } from "~/types/domain/department";
import type { ProblemType } from "~/types/domain/problem";

definePageMeta({
  name: "Editing department",
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;
const departmentId = route.params.departmentId as string;

const {
  loading: departmentCreationLoading,
  updateDepartment,
  getDepartmentById,
} = useDepartments();

const { data: department, pending: pendingForDepartments } =
  await getDepartmentById(municipalityId, departmentId);

useHead({
  title: department.value
    ? `Vizo | Editing ${department.value.name}`
    : "Vizo | Editing a department",
  meta: [
    {
      name: "description",
      content: "Edit an existing department in this municipality.",
    },
  ],
});

const { getUsersAssignedToDepartment } = useAssignedUsers();

const { data: assignees, pending: pendingForAssignees } =
  await getUsersAssignedToDepartment(municipalityId, departmentId, {
    page: 0,
    size: 100,
  });

const state = computed<
  (Department & { assignees: AffiliatedUser[] }) | undefined
>(() => {
  if (!department.value) return undefined;

  if (!assignees.value)
    return {
      ...department.value,
      assignees: [],
    };

  return {
    ...department.value,
    assignees: assignees.value.content.map((a) => a.user),
  };
});

const { loading: userAssignmentLoading, assignUsersToDepartment } =
  useAssignedUsers();

const toast = useToast();

async function onSubmit(data: {
  name: string;
  colorHex: string;
  selectedFiliates: AffiliatedUser[];
  problemTypes: ProblemType[];
  iconUrl?: string;
}) {
  const department = await updateDepartment(municipalityId, departmentId, {
    name: data.name,
    iconUrl: data.iconUrl,
    colorHex: data.colorHex,
    problemTypes: data.problemTypes,
  });

  if (!department) return;

  if (data.selectedFiliates.length > 0) {
    const assignments = await assignUsersToDepartment(
      municipalityId,
      department.id,
      {
        affiliationsIds: data.selectedFiliates.map((f) => f.id),
      }
    );

    if (!assignments) return;
  }

  toast.add({
    title: "Success",
    description: "Department updated successfully!",
    color: "success",
  });

  await navigateTo(`/municipalities/${municipalityId}/departments`);
}
</script>

<template>
  <EmptyMessage v-if="pendingForDepartments || pendingForAssignees"
    >Loading...</EmptyMessage
  >
  <EmptyMessage v-else-if="!department">Department not found.</EmptyMessage>
  <DepartmentsForm
    v-else
    :title="`Editing ${department.name}`"
    :description="`Edit the ${department.name} department in ${department.municipality.name}`"
    :state="state"
    @submit="onSubmit"
    :loading="departmentCreationLoading || userAssignmentLoading"
  />
</template>
