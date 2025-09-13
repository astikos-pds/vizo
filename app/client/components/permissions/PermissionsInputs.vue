<script lang="ts" setup>
import type { Permission } from "~/types/domain/permission";

const model = defineModel<Permission>({ required: true });

defineProps<{
  disabled?: boolean;
}>();
</script>

<template>
  <section class="flex flex-col gap-5">
    <UFormField
      label="Can view reports"
      name="can-view-reports"
      description="This permits that a filiate view all reports for a problem"
      required
      class="w-full flex justify-between items-center"
    >
      <USwitch v-model="model.canViewReports" :disabled="disabled" />
    </UFormField>

    <USeparator />

    <UFormField
      label="Can update status"
      name="can-update-status"
      description="This permits that a filiate changes a problem's status"
      required
      class="w-full flex justify-between items-center"
    >
      <USwitch v-model="model.canUpdateStatus" :disabled="disabled" />
    </UFormField>

    <USeparator />

    <UFormField
      label="Can manage users"
      name="can-manage-users"
      description="This permits that a filiate changes the permission of other assignees in a department"
      required
      class="w-full flex justify-between items-center"
    >
      <USwitch v-model="model.canManageUsers" :disabled="disabled" />
    </UFormField>
  </section>
</template>
