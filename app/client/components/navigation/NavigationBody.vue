<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { t } = useI18n();

const items = computed<NavigationMenuItem[][]>(() => {
  return [
    [
      {
        label: t("navBar.index"),
        icon: "i-lucide-house",
        to: "/",
      },
      {
        label: t("navBar.report"),
        icon: "i-lucide-message-square-warning",
        to: "/report",
      },
      {
        label: "Points of interest",
        icon: "i-lucide-map-pin",
        to: "/points-of-interest",
        children: [
          {
            label: "View all",
            icon: "i-lucide-map",
            to: "/points-of-interest",
          },
          {
            label: "Create new",
            icon: "i-lucide-plus",
            to: "/points-of-interest/new",
          },
        ],
      },
      {
        label: "Affiliations",
        icon: "i-lucide-archive",
        to: "/affiliations",
        children: [
          {
            label: "View all",
            icon: "i-lucide-building",
            to: "/affiliations",
          },
          {
            label: "Request new",
            icon: "i-lucide-git-pull-request",
            to: "/affiliations/request",
          },
        ],
      },
    ],
    [
      {
        label: "Settings",
        icon: "i-lucide-settings",
        to: "/settings",
      },
    ],
  ];

  /*
  return [
    [
      {
        label: "Dashboard",
        icon: "i-lucide-layout-dashboard",
        to: "/dashboard",
      },
      {
        label: "Problems",
        icon: "i-lucide-badge-alert",
        to: "/problems",
      },
    ],
    [...baseItems],
  ];
  */
});
</script>

<template>
  <div class="flex-1 w-full flex flex-col">
    <UNavigationMenu
      :collapsed="collapsed"
      :items="items"
      orientation="vertical"
      tooltip
    />
  </div>
</template>
