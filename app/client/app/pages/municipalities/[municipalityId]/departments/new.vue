<script lang="ts" setup>
import DepartmentsForm from "~/components/departments/DepartmentsForm.vue";
import { useAssignedUsers } from "~/composables/use-assigned-users";
import { useDepartments } from "~/composables/use-departments";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { ProblemType } from "~/types/domain/problem";

const { t } = useI18n();

useHead({
  title: t("head.newDepartment.title"),
  meta: [
    {
      name: "description",
      content: t("head.newDepartment.description"),
    },
  ],
});

definePageMeta({
  name: "New department",
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { loading: departmentCreationLoading, createDepartment } =
  useDepartments();
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
  const department = await createDepartment(municipalityId, {
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
    title: t("pages.newDepartment.success.title"),
    description: t("pages.newDepartment.success.description"),
    color: "success",
  });

  await navigateTo(`/municipalities/${municipalityId}/departments`);
}

const { currentAffiliation } = useLoggedInUserStore();
</script>

<template>
  <DepartmentsForm
    v-if="currentAffiliation"
    :title="t('pages.newDepartment.title')"
    :description="
      t('pages.newDepartment.description', {
        municipalityName: currentAffiliation.municipality.name,
      })
    "
    @submit="onSubmit"
    :loading="departmentCreationLoading || userAssignmentLoading"
  />
</template>
