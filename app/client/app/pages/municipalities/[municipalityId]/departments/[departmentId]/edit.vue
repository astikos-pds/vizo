<script lang="ts" setup>
import DepartmentsForm from "~/components/departments/DepartmentsForm.vue";
import { useAssignedUsers } from "~/composables/use-assigned-users";
import { useDepartments } from "~/composables/use-departments";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Department } from "~/types/domain/department";
import type { ProblemType } from "~/types/domain/problem";

const { t } = useI18n();

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
    ? t("head.editingDepartment.title", {
        departmentName: department.value.name,
      })
    : t("head.editingDepartment.title", { departmentName: "department" }),
  meta: [
    {
      name: "description",
      content: t("head.editingDepartment.description"),
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
  const updatedDepartment = await updateDepartment(
    municipalityId,
    departmentId,
    {
      name: data.name,
      iconUrl: data.iconUrl,
      colorHex: data.colorHex,
      problemTypes: data.problemTypes,
    }
  );

  if (!updatedDepartment) return;

  if (data.selectedFiliates.length > 0) {
    const assignments = await assignUsersToDepartment(
      municipalityId,
      updatedDepartment.id,
      {
        affiliationsIds: data.selectedFiliates.map((f) => f.id),
      }
    );

    if (!assignments) return;
  }

  toast.add({
    title: t("pages.editDepartment.success.title"),
    description: t("pages.editDepartment.success.description"),
    color: "success",
  });

  await navigateTo(`/municipalities/${municipalityId}/departments`);
}
</script>

<template>
  <EmptyMessage v-if="pendingForDepartments || pendingForAssignees">
    {{ t("pages.editDepartment.loading") }}
  </EmptyMessage>
  <EmptyMessage v-else-if="!department">{{
    t("pages.editDepartment.notFound")
  }}</EmptyMessage>
  <DepartmentsForm
    v-else
    :title="
      t('pages.editDepartment.title', { departmentName: department.name })
    "
    :description="
      t('pages.editDepartment.description', {
        departmentName: department.name,
        municipalityName: department.municipality.name,
      })
    "
    :state="state"
    @submit="onSubmit"
    :loading="departmentCreationLoading || userAssignmentLoading"
  />
</template>
