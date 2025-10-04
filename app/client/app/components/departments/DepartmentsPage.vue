<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

defineProps<{
  title: string;
  description: String;
}>();

const { currentAffiliation } = useLoggedInUserStore();

const items = computed<NavigationMenuItem[]>(() => {
  if (!currentAffiliation) return [];

  return [
    {
      label: t("components.navigation.viewAll"),
      icon: "i-lucide-package-open",
      to: `/municipalities/${currentAffiliation.municipality.id}/departments`,
    },
    {
      label: t("components.navigation.createNew"),
      icon: "i-lucide-square-plus",
      to: `/municipalities/${currentAffiliation.municipality.id}/departments/new`,
    },
  ];
});
</script>

<template>
  <CommonPage
    v-if="currentAffiliation"
    :title="title"
    :toolbar-items="currentAffiliation.isAdmin ? items : []"
    with-padding
  >
    <section class="size-full flex flex-col items-center">
      <main
        class="flex-1 w-[90%] lg:w-[80%] 2xl:w-[70%] mt-6 gap-3 flex flex-col items-center"
      >
        <div class="text-center flex flex-col gap-1">
          <h1 class="text-2xl font-semibold">{{ title }}</h1>
          <p class="text-sm">{{ description }}</p>
        </div>
        <div class="size-full flex flex-col items-center">
          <slot />
        </div>
      </main>
    </section>
  </CommonPage>
</template>
