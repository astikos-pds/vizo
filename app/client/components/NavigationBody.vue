<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { useAuth } from "~/composables/use-auth";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { t } = useI18n();
const { logout } = useAuth();

const items = computed<NavigationMenuItem[][]>(() => [
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
      label: t("navBar.settings"),
      icon: "i-lucide-settings",
      to: "/settings",
    },
  ],
  [
    {
      label: t("navBar.exit"),
      icon: "i-lucide-log-out",
      to: "/login",
      onSelect: (_) => logout(),
      class: "cursor-pointer",
    },
  ],
]);
</script>

<template>
  <div
    class="h-full p-3 md:h-[calc(100vh-5rem)] w-full flex flex-col justify-between"
  >
    <UNavigationMenu
      :collapsed="collapsed"
      :items="items[0]"
      orientation="vertical"
      tooltip
    />

    <UNavigationMenu
      :collapsed="collapsed"
      :items="items[1]"
      orientation="vertical"
      tooltip
    />
  </div>
</template>
