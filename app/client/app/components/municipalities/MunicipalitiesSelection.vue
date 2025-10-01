<script lang="ts" setup>
import type { AvatarProps, DropdownMenuItem } from "@nuxt/ui";

const { t } = useI18n();

defineProps<{
  collapsed?: boolean;
}>();

const userStore = useLoggedInUserStore();
const currentAffiliation = computed(() => userStore.currentAffiliation);
const affiliations = computed(() => userStore.affiliations);

async function close() {
  userStore.setCurrentAssignment(null);
  userStore.setAssignments([]);
  userStore.setCurrentAffiliation(null);
  userStore.setAffiliations([]);

  await navigateTo("/affiliations");
}

const items = computed<DropdownMenuItem[][]>(() => {
  const base = affiliations.value.map((a) => {
    return {
      label: a.municipality.name,
      avatar: {
        src: a.municipality.iconUrl?.toString(),
        alt: a.municipality.name,
      },
      to: `/municipalities/${a.municipality.id}`,
    };
  });

  return [
    base,
    [
      {
        label: t('components.municipalities.close'),
        icon: "i-lucide-circle-x",
        onSelect: () => close(),
      },
    ],
  ];
});

const avatar = computed<AvatarProps | undefined>(() => {
  if (!currentAffiliation.value) return undefined;

  return {
    src: currentAffiliation.value.municipality.iconUrl?.toString(),
    alt: currentAffiliation.value.municipality.name,
  };
});
</script>

<template>
  <UDropdownMenu
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
      >{{ collapsed ? "" : currentAffiliation?.municipality.name }}</UButton
    >
  </UDropdownMenu>
</template>
