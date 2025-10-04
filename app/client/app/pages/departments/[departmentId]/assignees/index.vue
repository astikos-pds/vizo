<script lang="ts" setup>
import type { AssignedUser } from "~/types/domain/assigned-user";

const { t } = useI18n();

useHead({
  title: t("head.assignedUsers.title"),
  meta: [
    {
      name: "description",
      content: t("head.assignedUsers.description"),
    },
  ],
});

definePageMeta({
  name: "Assigned users",
  middleware: ["auth", "assigned"],
});

const { currentAffiliation } = useLoggedInUserStore();

const { getPermissionPresetsInMunicipality } = usePermissionPresets();

const { data: permissionPresets, pending: pendingForPresets } =
  await getPermissionPresetsInMunicipality(
    currentAffiliation ? currentAffiliation.municipality.id : ""
  );

const selectedAssignee = ref<AssignedUser | undefined>(undefined);

const breakpoints = useBreakpoints({
  lg: 1024,
});

const isMobile = breakpoints.smallerOrEqual("lg");

const snapPoints = [0.3, 0.5, 0.9];
const activeSnapPoint = ref(snapPoints[0]);

const open = computed(() => isMobile.value);
</script>

<template>
  <AssignedUsersPage>
    <div class="size-full flex">
      <UDrawer
        v-if="isMobile"
        v-model:open="open"
        direction="bottom"
        :overlay="false"
        :dismissible="false"
        :modal="false"
        handle-only
        :snap-points="snapPoints"
        :active-snap-point="activeSnapPoint"
        class="min-h-screen"
      >
        <template #body>
          <AssignedUsersList v-model="selectedAssignee" />
        </template>
      </UDrawer>
      <aside v-else class="w-[40%] 2xl:w-[30%] border-r border-default">
        <AssignedUsersList v-model="selectedAssignee" />
      </aside>
      <div class="flex-1">
        <div
          class="size-full flex justify-center items-start mt-10 lg:items-center lg:mt-0"
        >
          <UIcon
            v-if="!selectedAssignee"
            name="lucide:contact"
            mode="svg"
            class="size-30"
          />
          <EmptyMessage v-else-if="pendingForPresets">{{
            t("pages.assignedUsers.loading")
          }}</EmptyMessage>
          <EmptyMessage v-else-if="permissionPresets === undefined">
            {{ t("pages.assignedUsers.failedToFetch") }}
          </EmptyMessage>
          <AssignedUsersDetailsCard
            v-else
            :assigned-user="selectedAssignee"
            :permission-presets="permissionPresets"
            @close="() => (selectedAssignee = undefined)"
            class="w-[80%]"
          />
        </div>
      </div>
    </div>
  </AssignedUsersPage>
</template>
