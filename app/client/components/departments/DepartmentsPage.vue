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
<<<<<<< HEAD
      label: t("components.navigation.viewAll"),
=======
      label: t('components.navigation.viewAll'),
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
      icon: "i-lucide-package-open",
      to: `/municipalities/${currentAffiliation.municipality.id}/departments`,
    },
    {
<<<<<<< HEAD
      label: t("components.navigation.createNew"),
=======
      label: t('components.navigation.createNew'),
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
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
      class="flex-1 w-[85%] lg:w-[70%] 2xl:w-[55%] p-3 my-8 gap-3 flex flex-col items-center"
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
