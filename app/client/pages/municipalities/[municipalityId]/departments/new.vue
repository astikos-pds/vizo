<script lang="ts" setup>
import DepartmentsForm from "~/components/departments/DepartmentsForm.vue";
import { useAssignedUsers } from "~/composables/use-assigned-users";
import { useDepartments } from "~/composables/use-departments";
import { useImageUpload } from "~/composables/use-image-upload";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
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

const { loading: imageUploadLoading, uploadImage } = useImageUpload();
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
  icon?: File | undefined;
}) {
  const icon = data.icon;

  const uploadedUrl = await uploadImage({
    file: icon,
  });
  const iconUrl = uploadedUrl.trim();

  const department = await createDepartment(municipalityId, {
    name: data.name,
    iconUrl: iconUrl.length === 0 ? undefined : iconUrl,
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

const { currentAffiliation } = useLoggedInUserStore();
</script>

<template>
  <DepartmentsForm
    v-if="currentAffiliation"
    title="New department"
    :description="`Create a new department in ${currentAffiliation.municipality.name}`"
    @submit="onSubmit"
    :loading="
      imageUploadLoading || departmentCreationLoading || userAssignmentLoading
    "
  />
</template>
