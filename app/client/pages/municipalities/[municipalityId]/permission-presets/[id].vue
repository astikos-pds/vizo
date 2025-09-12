<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const route = useRoute();
const municipalityId = route.params.municipalityId as string;
const permissionPresetId = route.params.id as string;

const { currentAffiliation } = useLoggedInUserStore();

const { getPermissionPresetInMunicipality } = usePermissionPresets();

const { data: permissionPreset, pending } =
  await getPermissionPresetInMunicipality(municipalityId, permissionPresetId);

const {
  loading,
  existsPermissionPresetByParamsInMunicipality,
  updatePermissionPreset,
} = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
  if (!permissionPreset.value) return;

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
  <PermissionPresetsPage v-if="pending || !permissionPreset">
    <EmptyMessage v-if="pending">Loading...</EmptyMessage>
    <EmptyMessage v-else-if="!permissionPreset">
      Point of interest not found.
    </EmptyMessage>
  </PermissionPresetsPage>
  <PermissionPresetsForm
    v-else
    :title="`Editing the ${permissionPreset.name} preset`"
    :state="permissionPreset"
    :loading="loading"
    editing
    @submit="onSubmit"
  />
</template>
