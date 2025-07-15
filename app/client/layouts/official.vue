<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import type { Municipality } from "~/types/domain";

const { t } = useI18n();

const route = useRoute();
const municipalityId = computed(() => route.params.municipalityId as string);

// const municipality = useNuxtData<Municipality>(`municipality-${municipalityId}`)
const municipality: Municipality = {
  id: "",
  name: "São Paulo",
  emailDomain: "",
  iconUrl: "",
  createdAt: "",
  updatedAt: "",
};

const { isAdmin } = useUserStore();

const items = computed<NavigationMenuItem[]>(() => {
  const baseItems: NavigationMenuItem[] = [];

  if (!municipalityId.value) {
    return baseItems;
  }

  const nestedItems = [
    {
      label: t("municipalitiesId.navigation.menu"),
      icon: "i-lucide-menu",
      to: `/municipalities/${municipalityId.value}`,
    },
    {
      label: t("municipalitiesId.navigation.departments"),
      icon: "i-lucide-box",
      to: `/municipalities/${municipalityId.value}/departments`,
    },
    {
      label: t("municipalitiesId.navigation.officials"),
      icon: "i-lucide-users",
      to: `/municipalities/${municipalityId.value}/officials`,
    },
  ];

  if (!isAdmin) {
    return [...baseItems, ...nestedItems];
  }

  const adminItems = [
    {
      label: t("municipalitiesId.navigation.affiliations"),
      icon: "i-lucide-contact",
      to: `/municipalities/${municipalityId}/affiliations`,
    },
  ];

  return [...baseItems, ...nestedItems, ...adminItems];
});
</script>

<template>
  <div class="h-screen flex flex-col">
    <ConfigHeader class="w-full" />

    <div
      class="w-full flex justify-between items-center border-b border-default"
    >
      <NavigationFooter collapsed />

      <UNavigationMenu v-if="items.length > 0" :items="items" />

      <UButton
        v-if="municipalityId"
        size="xl"
        variant="link"
        to="/municipalities"
        :avatar="{
          src: municipality.iconUrl,
          alt: municipality.name,
          size: 'md',
        }"
      />
    </div>

    <div class="w-full flex flex-col items-center justify-center">
      <slot />
    </div>
  </div>
</template>
