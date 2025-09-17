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
  name: "New permission preset",
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();

const {
  loading,
  getPermissionPresetInMunicipalityByParams,
  createPermissionPreset,
} = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
  const existingPreset = await getPermissionPresetInMunicipalityByParams(
    municipalityId,
    { name: data.name }
  );
  if (existingPreset) {
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

  toast.clear();

  toast.add({
    title: "Success",
    description: "Permission preset created successfully!",
    color: "success",
  });

  await refreshNuxtData(`municipalities-${municipalityId}-permission-presets`);
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
