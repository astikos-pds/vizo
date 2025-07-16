<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { useAuth } from "~/composables/use-auth";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { t } = useI18n();

const { isAdmin } = useUserStore();

const items = computed<NavigationMenuItem[][]>(() => {
  const baseItems = [
    {
      label: "Settings",
      icon: "i-lucide-settings",
      to: "/settings",
    },
  ];

  if (!isAdmin) {
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
      ],
      [...baseItems],
    ];
  }

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
        to: "/dashboard",
      },
    ],
    [...baseItems],
  ];
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
