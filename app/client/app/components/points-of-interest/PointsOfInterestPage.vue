<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

const items = ref<NavigationMenuItem[]>([
  {
    label: "View all",
    icon: "i-lucide-map",
    to: "/points-of-interest",
  },
  {
    label: "Create new",
    icon: "i-lucide-map-pin-plus",
    to: "/points-of-interest/new",
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
  <CommonPage title="Points of interest" :toolbar-items="items">
    <main class="flex-1 flex overflow-hidden">
      <UDrawer
        v-if="isMobile"
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
      <aside v-else class="w-[40%] 2xl:w-[30%] border-r border-default">
        <section
          class="size-full flex flex-col justify-between overflow-y-auto"
        >
          <slot name="aside" />
        </section>
      </aside>
      <div class="flex-1">
        <slot name="main" />
      </div>
    </main>
  </CommonPage>
</template>
