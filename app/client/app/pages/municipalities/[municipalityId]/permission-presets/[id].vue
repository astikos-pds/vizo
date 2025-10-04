<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const { t } = useI18n();

definePageMeta({
  name: "Permission preset",
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;
const permissionPresetId = route.params.id as string;

const { getPermissionPresetInMunicipality } = usePermissionPresets();

const { data: permissionPreset, pending } =
  await getPermissionPresetInMunicipality(municipalityId, permissionPresetId);

useHead({
  title: permissionPreset.value
    ? t("head.editPermissionPreset.title", {
        name: permissionPreset.value.name,
      })
    : t("head.editPermissionPreset.title", { name: "permission preset" }),
  meta: [
    {
      name: "description",
      content: t("head.editPermissionPreset.description"),
    },
  ],
});

const {
  loading,
  getPermissionPresetInMunicipalityByParams,
  updatePermissionPreset,
} = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
  if (!permissionPreset.value) return;

  const existingPreset = await getPermissionPresetInMunicipalityByParams(
    municipalityId,
    { name: data.name }
  );

  if (existingPreset && existingPreset.id !== permissionPreset.value.id) {
    toast.clear();

    toast.add({
      title: t("pages.permissionPresets.nameInUse.title"),
      description: t("pages.permissionPresets.nameInUse.description"),
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

  toast.clear();

  toast.add({
    title: t("pages.permissionPresets.success.updated.title"),
    description: t("pages.permissionPresets.success.updated.description"),
    color: "success",
  });

  permissionPreset.value = updated;

  await refreshNuxtData(`municipalities-${municipalityId}-permission-presets`);
};
</script>

<template>
  <PermissionPresetsPage
    title="Permission preset"
    v-if="pending || !permissionPreset"
  >
    <EmptyMessage v-if="pending">{{ t("common.loading") }}</EmptyMessage>
    <EmptyMessage v-else-if="!permissionPreset">
      {{ t("pages.permissionPresets.notFound") }}
    </EmptyMessage>
  </PermissionPresetsPage>
  <PermissionPresetsForm
    v-else
    :title="
      t('pages.permissionPresets.editTitle', { name: permissionPreset.name })
    "
    :state="permissionPreset"
    :loading="loading"
    editing
    @submit="onSubmit"
  />
</template>
