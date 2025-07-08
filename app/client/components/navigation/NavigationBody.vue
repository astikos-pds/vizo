<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { useAuth } from "~/composables/use-auth";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { t } = useI18n();
const { logout } = useAuth();

const items = computed<NavigationMenuItem[]>(() => [
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
]);
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
