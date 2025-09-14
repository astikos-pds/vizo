<script lang="ts" setup>
import type { FormSubmitEvent, SelectMenuItem } from "@nuxt/ui";
import z from "zod";
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { Permission, PermissionPreset } from "~/types/domain/permission";

const { locale } = useI18n();

const { assignedUser, permissionPresets } = defineProps<{
  assignedUser: AssignedUser;
  permissionPresets: PermissionPreset[];
}>();

const municipalityId = assignedUser.department.municipality.id;
const departmentId = assignedUser.department.id;

const emit = defineEmits<{
  close: [];
}>();

const assigneeSchema = z.object({
  mode: z.custom<string>(),
  permissionPresetId: z.string().optional(),
  customPermission: z.custom<Permission>(),
});

type AssigneeSchema = z.infer<typeof assigneeSchema>;

const form = reactive<AssigneeSchema>({
  mode:
    assignedUser.permissionMode === "PRESET" && assignedUser.permissionPreset
      ? assignedUser.permissionPreset.name
      : "CUSTOM",
  permissionPresetId: assignedUser.permissionPreset
    ? assignedUser.permissionPreset.id
    : undefined,
  customPermission: { ...assignedUser.customPermission },
});

const permissionItems = computed<SelectMenuItem[][]>(() => {
  return [
    [
      {
        label: "Custom",
        value: "CUSTOM",
      },
    ],
    permissionPresets.map((p) => {
      return {
        label: p.name,
        value: p.id,
      };
    }),
  ];
});

function onUpdateMode(newValue: string) {
  if (newValue === "CUSTOM") {
    form.permissionPresetId = undefined;
    form.customPermission = assignedUser.customPermission;
    return;
  }

  if (!permissionPresets) return;

  form.permissionPresetId = newValue;
}

const selectedPermissionPreset = computed(() =>
  permissionPresets.find((p) => p.id === form.permissionPresetId)
);

const effectivePermission = computed({
  get: () => {
    return form.mode !== "CUSTOM" && selectedPermissionPreset.value
      ? selectedPermissionPreset.value.permission
      : form.customPermission;
  },
  set: (newValue: Permission) => {
    form.customPermission = newValue;
  },
});

const hasUnsavedChanges = computed(() => {
  const modeChanged =
    (form.mode === "CUSTOM") !== (assignedUser.permissionMode === "CUSTOM");

  const permissionPresetChanged =
    form.permissionPresetId !== assignedUser.permissionPreset?.id;

  const customPermissionChanged =
    form.customPermission.canViewReports !==
      assignedUser.customPermission.canViewReports ||
    form.customPermission.canUpdateStatus !==
      assignedUser.customPermission.canUpdateStatus ||
    form.customPermission.canManageUsers !==
      assignedUser.customPermission.canManageUsers;

  return modeChanged || permissionPresetChanged || customPermissionChanged;
});

const { loading, changeAssigneePermission, removeAssigneeFromDepartment } =
  useAssignedUsers();
const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<AssigneeSchema>) => {
  if (!hasUnsavedChanges) return;

  const saved = await changeAssigneePermission(
    municipalityId,
    departmentId,
    assignedUser.id,
    {
      permissionMode: event.data.mode === "CUSTOM" ? "CUSTOM" : "PRESET",
      ...event.data,
    }
  );

  if (!saved) return;

  toast.add({
    title: "Changes saved",
    description: "Assignee permission saved successfully",
    color: "success",
  });

  await refreshNuxtData(
    `municipalities-${municipalityId}-departments-${departmentId}-assignments`
  );
};

const onDelete = async () => {
  await removeAssigneeFromDepartment(
    municipalityId,
    departmentId,
    assignedUser.id
  );

  await refreshNuxtData(
    `municipalities-${municipalityId}-departments-${departmentId}-assignments`
  );
};

const { currentAssignment } = useLoggedInUserStore();
const loggedInUserCanExecuteActions = computed(
  () =>
    currentAssignment &&
    currentAssignment.effectivePermission.canManageUsers &&
    assignedUser.id !== currentAssignment.id
);
</script>

<template>
  <UCard v-if="currentAssignment" color="neutral" varaint="outline">
    <template #header>
      <UButton
        color="neutral"
        variant="ghost"
        icon="i-lucide-x"
        class="text-2xl"
        @click="emit('close')"
      />
    </template>

    <div class="size-full flex flex-col gap-5 p-2">
      <div class="flex items-center">
        <AssignedUsersCardBody v-bind="assignedUser" />

        <UButton
          v-if="loggedInUserCanExecuteActions"
          color="error"
          variant="subtle"
          @click="onDelete"
          >Remove</UButton
        >
      </div>

      <div v-if="loggedInUserCanExecuteActions" class="flex flex-col gap-5">
        <USeparator />

        <main class="w-full px-2">
          <UForm
            :schema="assigneeSchema"
            :state="form"
            @submit="onSubmit"
            class="w-full flex flex-col items-center gap-5"
          >
            <UFormField
              label="Permission mode"
              name="permission-mode"
              class="w-full"
            >
              <USelectMenu
                v-model="form.mode"
                value-key="value"
                :items="permissionItems"
                :search-input="{
                  placeholder: 'Search',
                  icon: 'i-lucide-search',
                }"
                class="w-full"
                @update:model-value="onUpdateMode"
              />
            </UFormField>

            <PermissionsInputs
              v-model="effectivePermission"
              :disabled="form.mode !== 'CUSTOM'"
              class="w-full"
            />

            <UChip v-if="hasUnsavedChanges" color="info">
              <UButton
                color="neutral"
                variant="solid"
                :loading="loading"
                type="submit"
                >Save changes</UButton
              >
            </UChip>
            <UButton
              v-else
              color="neutral"
              variant="solid"
              :loading="loading"
              type="submit"
              >Save changes</UButton
            >
          </UForm>
        </main>
      </div>
    </div>
  </UCard>
</template>
