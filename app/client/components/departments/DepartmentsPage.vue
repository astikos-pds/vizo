<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  title: string;
  description: String;
}>();

const { currentAffiliation } = useLoggedInUserStore();

const items = computed<NavigationMenuItem[]>(() => {
  if (!currentAffiliation) return [];

  return [
    {
      label: "View all",
      icon: "i-lucide-package-open",
      to: `/municipalities/${currentAffiliation.municipality.id}/departments`,
    },
    {
      label: "Create new",
      icon: "i-lucide-square-plus",
      to: `/municipalities/${currentAffiliation.municipality.id}/departments/new`,
    },
  ];
});
</script>

<template>
  <section
    v-if="currentAffiliation"
    class="size-full flex flex-col items-center"
  >
    <header
      v-if="currentAffiliation.isAdmin"
      class="w-full border-b border-default px-2"
    >
      <UNavigationMenu :items="items" highlight />
    </header>
    <main
      class="flex-1 w-[90%] lg:w-[80%] 2xl:w-[75%] p-3 my-8 gap-3 flex flex-col items-center"
    >
      <div class="text-center flex flex-col gap-1">
        <h1 class="text-2xl font-semibold">{{ title }}</h1>
        <p class="text-sm">{{ description }}</p>
      </div>
      <div class="w-full flex flex-col items-center">
        <slot />
      </div>
    </main>
  </section>
</template>
