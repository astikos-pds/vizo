<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

const items = ref<NavigationMenuItem[]>([
  {
    label: t('components.reports.newProblem'),
    icon: "i-lucide-plus",
    to: "/reports/new",
  },
  {
    label: t('components.reports.history'),
    icon: "i-lucide-history",
    to: "/reports",
  },
]);

const breakpoints = useBreakpoints({
  lg: 1024,
});

const isMobile = breakpoints.smallerOrEqual("lg");

const snapPoints = [0.3, 0.5, 0.9];
const activeSnapPoint = ref(snapPoints[1]);

const open = computed(() => isMobile.value);
</script>

<template>
  <section class="size-full flex flex-col">
    <header class="w-full border-b border-default px-2">
      <UNavigationMenu :items="items" highlight />
    </header>
    <main class="flex-1 flex justify-center overflow-hidden">
      <UDrawer
        v-if="$slots.aside && isMobile"
        v-model:open="open"
        direction="bottom"
        :overlay="false"
        :dismissible="false"
        :modal="false"
        handle-only
        :snap-points="snapPoints"
        :active-snap-point="activeSnapPoint"
        class="min-h-screen"
      >
        <template #body>
          <slot name="aside" />
        </template>
      </UDrawer>
      <aside
        v-else-if="$slots.aside"
        class="w-[40%] 2xl:w-[30%] border-r border-default overflow-auto"
      >
        <slot name="aside" />
      </aside>

      <div class="flex-1 flex justify-center items-center overflow-auto">
        <slot />
      </div>
    </main>
  </section>
</template>
