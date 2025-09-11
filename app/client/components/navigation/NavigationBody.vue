<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import MunicipalitiesSelection from "../municipalities/MunicipalitiesSelection.vue";

const open = defineModel<boolean>("open");

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { t } = useI18n();

const commomItems = computed<NavigationMenuItem[]>(() => {
  return [
    {
      label: t("navBar.index"),
      icon: "i-lucide-house",
      to: "/",
      onSelect: () => (open.value = false),
    },
    {
      label: t("navBar.report"),
      icon: "i-lucide-message-square-warning",
      to: "/report",
      onSelect: () => (open.value = false),
    },
    {
      label: "Points of interest",
      icon: "i-lucide-map-pin",
      to: "/points-of-interest",
      onSelect: () => (open.value = false),
      children: [
        {
          label: "View all",
          icon: "i-lucide-map",
          to: "/points-of-interest",
          onSelect: () => (open.value = false),
        },
        {
          label: "Create new",
          icon: "i-lucide-plus",
          to: "/points-of-interest/new",
          onSelect: () => (open.value = false),
        },
      ],
    },
    {
      label: "Affiliations",
      icon: "i-lucide-archive",
      to: "/affiliations",
      onSelect: () => (open.value = false),
      children: [
        {
          label: "View all",
          icon: "i-lucide-building",
          to: "/affiliations",
          onSelect: () => (open.value = false),
        },
        {
          label: "Request new",
          icon: "i-lucide-git-pull-request",
          to: "/affiliations/request",
          onSelect: () => (open.value = false),
        },
      ],
    },
    {
      label: "Settings",
      icon: "i-lucide-settings",
      to: "/settings",
      onSelect: () => (open.value = false),
    },
  ];
});

const userStore = useLoggedInUserStore();
const currentAffiliation = computed(() => userStore.currentAffiliation);

const affiliatedItems = computed<NavigationMenuItem[]>(() => {
  if (!currentAffiliation.value) return [];

  const municipalityId = currentAffiliation.value.municipality.id;

  const items = [
    {
      label: "Departments",
      icon: "i-lucide-package",
      to: `/municipalities/${municipalityId}/departments`,
      onSelect: () => (open.value = false),
      children: currentAffiliation.value.isAdmin
        ? [
            {
              label: "View all",
              icon: "i-lucide-package-open",
              to: `/municipalities/${municipalityId}/departments`,
              onSelect: () => (open.value = false),
            },
            {
              label: "Create new",
              icon: "i-lucide-square-plus",
              to: `/municipalities/${municipalityId}/departments/new`,
              onSelect: () => (open.value = false),
            },
          ]
        : [],
    },
    {
      label: "Public agents",
      icon: "i-lucide-users",
      to: `/municipalities/${municipalityId}/public-agents`,
      onSelect: () => (open.value = false),
    },
  ];

  if (currentAffiliation.value.isAdmin) {
    items.push({
      label: "Affiliation requests",
      icon: "i-lucide-folder",
      to: `/municipalities/${municipalityId}/affiliations`,
      onSelect: () => (open.value = false),
    });
  }

  return items;
});
</script>

<template>
  <div class="flex-1 w-full flex flex-col items-center gap-2">
    <UNavigationMenu
      class="w-full"
      :collapsed="collapsed"
      :items="commomItems"
      orientation="vertical"
      highlight
      tooltip
    />

    <div
      class="w-full flex flex-col mt-2 gap-2 items-center"
      v-if="currentAffiliation && affiliatedItems.length !== 0"
    >
      <USeparator size="xs" />

      <MunicipalitiesSelection :collapsed="collapsed" class="w-full" />

      <UNavigationMenu
        class="w-full"
        :collapsed="collapsed"
        :items="affiliatedItems"
        orientation="vertical"
        highlight
        tooltip
      />
    </div>
  </div>
</template>
