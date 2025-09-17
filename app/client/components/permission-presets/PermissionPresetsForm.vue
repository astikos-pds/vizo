<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { PermissionPreset } from "~/types/domain/permission";

const { t } = useI18n();

const { state, editing } = defineProps<{
  title: string;
  state?: PermissionPreset;
  loading?: boolean;
  editing?: boolean;
}>();

const emit = defineEmits<{
  submit: [data: PermissionPresetSchema];
}>();

const permissionPresetSchema = z.object({
  name: z.string().min(1, t('components.permissionPresets.name')),
  canViewReports: z.boolean(),
  canUpdateStatus: z.boolean(),
  canManageUsers: z.boolean(),
});

type PermissionPresetSchema = z.infer<typeof permissionPresetSchema>;

const form = reactive<PermissionPresetSchema>({
  name: state ? state.name : "",
  canViewReports: state ? state.permission.canViewReports : false,
  canUpdateStatus: state ? state.permission.canUpdateStatus : false,
  canManageUsers: state ? state.permission.canManageUsers : false,
});

const { loading: loadingForDeleting, deletePermissionPreset } =
  usePermissionPresets();

const onDelete = async () => {
  if (!state) return;

  const municipalityId = state.municipality.id;
  const permissionPresetId = state.id;

  await deletePermissionPreset(municipalityId, permissionPresetId);

  await refreshNuxtData(`municipalities-${municipalityId}-permission-presets`);

  await navigateTo(`/municipalities/${municipalityId}/permission-presets`);
};

const onSubmit = async (event: FormSubmitEvent<PermissionPresetSchema>) => {
  if (state && !hasUnsavedChanges.value) return;

  emit("submit", event.data);
};

const hasUnsavedChanges = computed(() => {
  if (!editing || !state) return false;

  return (
    state.name !== form.name ||
    state.permission.canViewReports !== form.canViewReports ||
    state.permission.canUpdateStatus !== form.canUpdateStatus ||
    state.permission.canManageUsers !== form.canManageUsers
  );
});
</script>

<template>
  <PermissionPresetsPage>
    <UForm
      :schema="permissionPresetSchema"
      :state="form"
      :disabled="loadingForDeleting"
      @submit="onSubmit"
      class="w-[90%] xl:w-[70%] 2xl:w-[60%] flex flex-col justify-center items-center gap-4"
    >
      <header class="w-full flex justify-between items-center gap-4">
        <div class="flex flex-col">
          <h1 class="font-semibold text-lg">{{ title }}</h1>
          <span class="text-xs sm:text-sm"
            >{{ t('components.permissionPresets.permissionPresetDescription') }}</span
          >
        </div>
        <div class="flex-1 flex gap-2 items-center justify-end text-nowrap">
          <UButton
            v-if="editing"
            color="error"
            :disabled="loading"
            :loading="loadingForDeleting"
            @click="onDelete"
            >{{ t('components.permissionPresets.delete') }}</UButton
          >
          <UChip v-if="hasUnsavedChanges" color="info">
            <UButton
              :color="editing ? 'neutral' : 'primary'"
              type="submit"
              :loading="loading"
              >{{ editing ? t('components.permissionPresets.saveChanges') : t('components.permissionPresets.save') }}</UButton
            >
          </UChip>
          <UButton
            v-else
            :color="editing ? 'neutral' : 'primary'"
            type="submit"
            :loading="loading"
            >{{ editing ? t('components.permissionPresets.saveChanges') : t('components.permissionPresets.save') }}</UButton
          >
        </div>
      </header>

      <UCard color="neutral" variant="subtle" class="size-full">
        <div
          class="size-full flex flex-col justify-center items-center gap-5 p-3"
        >
          <UFormField
            :label="t('components.permissionPresets.name')"
            name="name"
            :description="t('components.permissionPresets.nameDescription')"
            required
            class="w-full flex flex-col justify-between gap-1 md:flex-row md:items-center"
          >
            <UInput
              v-model="form.name"
              :placeholder="t('components.permissionPresets.veryMemorableName')"
              class="w-full"
            />
          </UFormField>

          <USeparator />

          <PermissionsInputs v-model="form" class="w-full" />
        </div>
      </UCard>
    </UForm>
  </PermissionPresetsPage>
</template>
