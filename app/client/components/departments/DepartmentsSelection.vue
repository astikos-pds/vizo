<script lang="ts" setup>
import type { AvatarProps, DropdownMenuItem } from "@nuxt/ui";

const { t } = useI18n();

defineProps<{
  collapsed?: boolean;
}>();

const userStore = useLoggedInUserStore();
const currentAssignment = computed(() => userStore.currentAssignment);
const assignments = computed(() => userStore.assignments);

async function close() {
  const municipalityId = currentAssignment.value?.department.municipality.id;

  userStore.setCurrentAssignment(null);
  userStore.setAssignments([]);

  await navigateTo(
    municipalityId
      ? `/municipalities/${municipalityId}/departments`
      : "/affiliations"
  );
}

const items = computed<DropdownMenuItem[][]>(() => {
  const base = assignments.value.map((a) => {
    return {
      label: a.department.name,
      avatar: {
        src: a.department.iconUrl?.toString(),
        alt: a.department.name,
      },
      to: `/departments/${a.department.id}`,
    };
  });

  return [
    base,
    [
      {
        label: t('components.departments.close'),
        icon: "i-lucide-circle-x",
        onSelect: () => close(),
      },
    ],
  ];
});

const avatar = computed<AvatarProps | undefined>(() => {
  if (!currentAssignment.value) return undefined;

  return {
    src: currentAssignment.value.department.iconUrl?.toString(),
    alt: currentAssignment.value.department.name,
  };
});
</script>

<template>
  <UDropdownMenu
    v-if="currentAssignment"
    :items="items"
    :ui="{
      content: 'min-w-48',
    }"
  >
    <UButton
      color="neutral"
      variant="ghost"
      :avatar="avatar"
      :class="{
        'p-1 flex justify-center items-center': collapsed,
      }"
      >{{ collapsed ? "" : currentAssignment.department.name }}</UButton
    >
  </UDropdownMenu>
</template>
