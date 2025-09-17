<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const { t } = useI18n();

const model = defineModel<Permission>({ required: true });

defineProps<{
  disabled?: boolean;
}>();
</script>

<template>
  <section class="flex flex-col gap-5">
    <UFormField
      :label="t('components.permissions.canViewReports')"
      name="can-view-reports"
      :description="t('components.permissions.canViewReportsDescription')"
      required
      class="w-full flex justify-between items-center gap-3"
    >
      <USwitch v-model="model.canViewReports" :disabled="disabled" />
    </UFormField>

    <USeparator />

    <UFormField
      :label="t('components.permissions.canUpdateStatus')"
      name="can-update-status"
      :description="t('components.permissions.canUpdateStatusDescription')"
      required
      class="w-full flex justify-between items-center gap-3"
    >
      <USwitch v-model="model.canUpdateStatus" :disabled="disabled" />
    </UFormField>

    <USeparator />

    <UFormField
      :label="t('components.permissions.canManageUsers')"
      name="can-manage-users"
      :description="t('components.permissions.canManageUsersDescription')"
      required
      class="w-full flex justify-between items-center gap-3"
    >
      <USwitch v-model="model.canManageUsers" :disabled="disabled" />
    </UFormField>
  </section>
</template>
