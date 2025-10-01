<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const route = useRoute();
const departmentId = route.params.departmentId as string;

const { currentAffiliation } = useLoggedInUserStore();

const items = ref<NavigationMenuItem[]>([
  {
    label: "List",
    icon: "i-lucide-list",
    to: `/departments/${departmentId}/assignees`,
  },
  {
    label: "Assign",
    icon: "i-lucide-user-plus",
    to: `/departments/${departmentId}/assignees/add`,
  },
]);
</script>

<template>
  <section v-if="currentAffiliation" class="size-full flex flex-col">
    <header class="w-full border-b border-default px-2">
      <UNavigationMenu
        v-if="currentAffiliation.isAdmin"
        :items="items"
        highlight
      />
    </header>
    <main class="flex-1 flex overflow-hidden">
      <slot />
    </main>
  </section>
</template>
