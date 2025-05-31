<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const collapsed = ref<boolean>(false);

const items = ref<NavigationMenuItem[]>([
  {
    label: "Home",
    icon: "i-lucide-house",
    to: "/",
  },
  {
    label: "Report",
    icon: "i-lucide-message-square-warning",
    to: "/report",
  },
  {
    label: "Settings",
    icon: "i-lucide-settings",
  },
]);
</script>

<template>
  <div
    class="min-h-full p-4 border-x border-zinc-200"
    :class="collapsed ? '' : 'min-w-[20%]'"
  >
    <header class="h-[10%] flex justify-between items-center">
      <div v-if="!collapsed">Vizo</div>
      <UButton
        icon="i-lucide-menu"
        color="neutral"
        variant="outline"
        size="xl"
        class="hover:cursor-pointer size-[2.5rem] flex justify-center items-center text-[1.5rem]"
        @click="collapsed = !collapsed"
      />
    </header>
    <div class="h-[90%] flex flex-col justify-between">
      <main class="h-full flex flex-col">
        <UNavigationMenu
          :collapsed="collapsed"
          :items="items"
          orientation="vertical"
          :ui="{
            link: 'px-3 min-h-[3rem] text-[1.3rem] gap-4',
          }"
        />
      </main>
      <div class="h-[10%] flex items-center">
        <UButton
          icon="i-lucide-log-out"
          color="neutral"
          variant="ghost"
          class="w-full text-xl cursor-pointer"
          ><span v-if="!collapsed">Exit</span></UButton
        >
      </div>
    </div>
  </div>
</template>
