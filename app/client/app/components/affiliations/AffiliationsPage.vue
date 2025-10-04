<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  withPadding?: boolean;
}>();

const { t } = useI18n();

const items = ref<NavigationMenuItem[]>([
  {
    label: t("components.affiliations.viewAll"),
    icon: "i-lucide-building",
    to: "/affiliations",
  },
  {
    label: t("components.affiliations.requestNew"),
    icon: "i-lucide-git-pull-request",
    to: "/affiliations/request",
  },
]);
</script>

<template>
  <CommonPage
    title="Affiliations"
    :toolbar-items="items"
    :with-padding="withPadding"
  >
    <main class="flex-1 flex flex-col items-center">
      <slot />
    </main>
  </CommonPage>
</template>
