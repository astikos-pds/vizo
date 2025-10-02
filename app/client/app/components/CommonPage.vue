<script setup lang="ts">
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  title: string;
  withPadding?: boolean;
  toolbarItems?: NavigationMenuItem[];
}>();

const slots = useSlots();
</script>

<template>
  <UDashboardPanel
    :id="title.toLowerCase()"
    :ui="{ body: !withPadding ? 'p-0 sm:p-0' : '' }"
  >
    <template #header>
      <UDashboardNavbar :title="title" :ui="{ right: 'gap-3' }">
        <template #leading>
          <UDashboardSidebarCollapse :ui="{ base: 'text-xl' }" />
        </template>

        <template #right>
          <NavigationOptions />
        </template>
      </UDashboardNavbar>

      <UDashboardToolbar v-if="toolbarItems || slots.toolbar">
        <template #left>
          <slot name="toolbar">
            <UNavigationMenu :items="toolbarItems" highlight />
          </slot>
        </template>
      </UDashboardToolbar>
    </template>

    <template #body>
      <slot />
    </template>
  </UDashboardPanel>
</template>
