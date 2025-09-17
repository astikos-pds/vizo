<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

const items = ref<NavigationMenuItem[]>([
  {
    label: t('components.affiliations.viewAll'),
    icon: "i-lucide-building",
    to: "/affiliations",
  },
  {
    label: t('components.affiliations.requestNew'),
    icon: "i-lucide-git-pull-request",
    to: "/affiliations/request",
  },
]);
</script>

<template>
  <section class="size-full flex flex-col overflow-hidden">
    <header class="w-full border-b border-default px-2">
      <UNavigationMenu :items="items" highlight />
    </header>
    <main class="flex-1 flex flex-col items-center overflow-y-auto">
      <slot />
    </main>
  </section>
</template>
