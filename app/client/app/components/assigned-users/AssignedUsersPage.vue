<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  withPadding?: boolean;
}>();

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
  <CommonPage
    title="Assigned users"
    :toolbar-items="items"
    :with-padding="withPadding"
  >
    <section v-if="currentAffiliation" class="size-full flex flex-col">
      <main class="flex-1 flex overflow-hidden">
        <slot />
      </main>
    </section>
  </CommonPage>
</template>
