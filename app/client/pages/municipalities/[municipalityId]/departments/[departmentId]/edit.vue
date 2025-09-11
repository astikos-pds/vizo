<script lang="ts" setup>
import DepartmentsForm from "~/components/departments/DepartmentsForm.vue";
import { useAssignedUsers } from "~/composables/use-assigned-users";
import { useDepartments } from "~/composables/use-departments";
import { useImageUpload } from "~/composables/use-image-upload";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Department } from "~/types/domain/department";
import type { ProblemType } from "~/types/domain/problem";

useHead({
  title: "Vizo | New department",
  meta: [
    {
      name: "description",
      content: "Create a new department in this municipality.",
    },
  ],
});

definePageMeta({
  middleware: ["auth", "affiliated"],
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

const { loading: imageUploadLoading, uploadImage } = useImageUpload();
const { loading: userAssignmentLoading, assignUsersToDepartment } =
  useAssignedUsers();

const toast = useToast();

async function onSubmit(data: {
  name: string;
  colorHex: string;
  selectedFiliates: AffiliatedUser[];
  problemTypes: ProblemType[];
  icon?: File | undefined;
}) {
  const icon = data.icon;

  const iconUrl = await uploadImage({
    file: icon,
  });

  const department = await updateDepartment(municipalityId, departmentId, {
    name: data.name,
    iconUrl,
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
    description: "Department created successfully!",
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
    :description="`Edit the ${department.name} in ${department.municipality.name}`"
    :state="state"
    @submit="onSubmit"
    :loading="
      imageUploadLoading || departmentCreationLoading || userAssignmentLoading
    "
  />
</template>
