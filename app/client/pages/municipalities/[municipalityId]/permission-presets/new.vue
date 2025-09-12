<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

useHead({
  title: "Vizo | New permission preset",
  meta: [
    {
      name: "description",
      content: "Create a new permission preset in this municipality",
    },
  ],
});

definePageMeta({
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();

const {
  loading,
  existsPermissionPresetByParamsInMunicipality,
  createPermissionPreset,
} = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
  const nameAlreadyInUse = await existsPermissionPresetByParamsInMunicipality(
    municipalityId,
    { name: data.name }
  );
  if (nameAlreadyInUse === null) return;
  if (nameAlreadyInUse === true) {
    toast.clear();

    toast.add({
      title: "Name already in use",
      description:
        "A permission preset was found with this name in this municipality.",
      color: "error",
    });
    return;
  }

  const created = await createPermissionPreset(municipalityId, {
    name: data.name,
    permission: {
      ...data,
    },
  });

  if (!created) return;

  toast.add({
    title: "Success",
    description: "Permission preset created successfully!",
    color: "success",
  });

  await navigateTo(
    `/municipalities/${municipalityId}/permission-presets/${created.id}`
  );
};
</script>

<template>
  <PermissionPresetsForm
    v-if="currentAffiliation"
    title="New permission preset"
    :loading="loading"
    @submit="onSubmit"
  />
</template>
