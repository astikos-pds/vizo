<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

const route = useRoute();

const municipalityId = computed(() => route.params.municipalityId as string);

const { isAdmin } = useUserStore();

const items = computed<NavigationMenuItem[]>(() => {
  const baseItems: NavigationMenuItem[] = [
    {
      label: "Municipalidades",
      icon: "i-lucide-building",
      to: "/municipalities",
    },
  ];

  if (!municipalityId.value) {
    return baseItems;
  }

  const nestedItems = [
    {
      icon: "i-lucide-chevron-right",
    },
    {
      label: t("municipalitiesId.navigation.menu"),
      icon: "i-lucide-menu",
      to: `/municipalities/${municipalityId.value}`,
    },
    {
      label: t("municipalitiesId.navigation.departments"),
      icon: "i-lucide-box",
      to: `/municipalities/${municipalityId.value}/departments`,
    },
    {
      label: t("municipalitiesId.navigation.officials"),
      icon: "i-lucide-users",
      to: `/municipalities/${municipalityId.value}/officials`,
    },
  ];

  if (!isAdmin) {
    return [...baseItems, ...nestedItems];
  }

  const adminItems = [
    {
      label: t("municipalitiesId.navigation.affiliations"),
      icon: "i-lucide-contact",
      to: `/municipalities/${municipalityId}/affiliations`,
    },
  ];

  return [...baseItems, ...nestedItems, ...adminItems];
});
</script>

<template>
  <div class="h-screen flex flex-col">
    <ConfigHeader class="w-full" />

    <UNavigationMenu
      v-if="items.length > 0"
      :items="items"
      class="w-full flex justify-center border-b border-default"
    />

    <div class="w-full flex flex-col items-center justify-center">
      <slot />
    </div>
  </div>
</template>
