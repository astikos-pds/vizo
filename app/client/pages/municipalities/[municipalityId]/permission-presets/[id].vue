<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const route = useRoute();
const municipalityId = route.params.municipalityId as string;
const permissionPresetId = route.params.id as string;

const { currentAffiliation } = useLoggedInUserStore();

const { getPermissionPresetInMunicipality } = usePermissionPresets();

const { data: permissionPreset, pending } =
  await getPermissionPresetInMunicipality(municipalityId, permissionPresetId);

const { loading, updatePermissionPreset } = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
  if (!permissionPreset.value) return;

  const updated = await updatePermissionPreset(
    municipalityId,
    permissionPreset.value.id,
    {
      name: data.name,
      permission: {
        ...data,
      },
    }
  );

  if (!updated) return;

  toast.add({
    title: "Success",
    description: "Permission preset updated successfully!",
    color: "success",
  });

  permissionPreset.value = updated;

  await refreshNuxtData(`municipalities-${municipalityId}-permission-presets`);
};
</script>

<template>
  <PermissionPresetsForm
    v-if="currentAffiliation && permissionPreset"
    :title="`Editing the ${permissionPreset.name} preset`"
    :state="permissionPreset"
    :loading="loading || pending"
    editing
    @submit="onSubmit"
  />
</template>
