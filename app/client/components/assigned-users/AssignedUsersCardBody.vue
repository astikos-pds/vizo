<script lang="ts" setup>
import type { AssignedUser } from "~/types/domain/assigned-user";

const { locale } = useI18n();

const assignedUser = defineProps<AssignedUser>();

const effectivePermissions =
  assignedUser.permissionMode === "PRESET" && assignedUser.permissionPreset
    ? assignedUser.permissionPreset.permission
    : assignedUser.customPermission;

const cardLabel = computed<Record<string, string>>(() => {
  return {
    canViewReports: "Can view reports",
    canUpdateStatus: "Can update status",
    canManageUsers: "Can manage users",
  };
});
</script>

<template>
  <div class="size-full flex gap-3">
    <div class="h-full justify-center items-center">
      <UAvatar
        :src="assignedUser.user.user.avatarUrl?.toString()"
        :alt="assignedUser.user.user.name"
        size="2xl"
      />
    </div>
    <div class="size-full flex flex-col gap-2">
      <header class="flex justify-start flex-col">
        <div class="flex gap-2 items-center">
          <span class="text-base font-medium">{{
            assignedUser.user.user.name
          }}</span>

          <UBadge color="neutral" variant="soft" size="sm">You</UBadge>
        </div>
        <span class="text-xs 2xl:text-sm">{{
          assignedUser.user.institutionalEmail
        }}</span>
      </header>
      <main class="flex flex-col justify-start gap-2">
        <span class="text-xs"
          >Assigned at
          {{
            assignedUser.assignedAt.toLocaleDateString(locale, {
              dateStyle: "long",
            })
          }}</span
        >
        <div class="flex gap-1 flex-wrap">
          <template
            v-for="(hasPermission, key) in effectivePermissions"
            :key="key"
          >
            <UBadge
              v-if="hasPermission"
              color="neutral"
              variant="subtle"
              size="sm"
            >
              {{ cardLabel[key] }}
            </UBadge>
          </template>
        </div>
      </main>
    </div>
  </div>
</template>
