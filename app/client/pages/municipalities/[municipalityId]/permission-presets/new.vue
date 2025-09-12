<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();

const { loading, createPermissionPreset } = usePermissionPresets();
const toast = useToast();

const onSubmit = async (data: Permission & { name: string }) => {
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
