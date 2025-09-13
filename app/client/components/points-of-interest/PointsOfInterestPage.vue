<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  title: string;
}>();

const items = ref<NavigationMenuItem[]>([
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
]);
</script>

<template>
  <section class="size-full flex flex-col">
    <header class="w-full border-b border-default px-2">
      <UNavigationMenu :items="items" highlight />
    </header>
    <main class="flex-1 flex overflow-hidden">
      <aside class="w-[40%] 2xl:w-[30%] border-r border-default">
        <section
          class="size-full flex flex-col justify-between overflow-y-auto"
        >
          <header class="p-3 text-center border-b border-default">
            <h1 class="text-xl font-semibold">{{ title }}</h1>
          </header>
          <slot name="aside" />
        </section>
      </aside>
      <div class="flex-1">
        <slot name="main" />
      </div>
    </main>
  </section>
</template>
