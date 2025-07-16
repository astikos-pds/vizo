<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import UserProfile from "~/components/navigation/UserProfile.vue";
import { municipalityRepository } from "~/repositories/municipality-repository";

const { t } = useI18n();

const route = useRoute();
const municipalityId = computed(() => route.params.municipalityId as string);

const { data: municipality, execute } = municipalityRepository.getById(
  municipalityId.value,
  {
    key: `municipality-${municipalityId.value}`,
    immediate: false,
  }
);

watch(municipalityId, (id) => {
  console.log(id);
  if (id) {
    execute();
  }
});

const { isAdmin } = useUserStore();

const navigationItems = computed<NavigationMenuItem[]>(() => {
  const baseItems: NavigationMenuItem[] = [];

  if (!municipalityId.value) {
    return baseItems;
  }

  const nestedItems = [
    {
      label: t("municipalitiesId.navigation.menu"),
      icon: "i-lucide-house",
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
      to: `/municipalities/${municipalityId.value}/affiliations`,
    },
  ];

  return [...baseItems, ...nestedItems, ...adminItems];
});

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");

const items = computed<NavigationMenuItem[]>(() => {
  if (!isMobile.value) {
    return navigationItems.value;
  }

  return [
    {
      icon: "i-lucide-menu",
      children: navigationItems.value,
    },
  ];
});
</script>

<template>
  <div class="h-screen flex flex-col">
    <ConfigHeader class="w-full" />

    <div
      class="w-full flex justify-between items-center border-b border-default p-1 2xl:p-2"
    >
      <UserProfile collapsed />

      <UNavigationMenu
        v-if="items.length > 0"
        :items="items"
        class="w-full flex justify-center items-center"
      />

      <UButton
        v-if="municipalityId"
        size="xl"
        variant="link"
        to="/municipalities"
        :avatar="{
          src: municipality?.iconUrl,
          alt: municipality?.name,
          size: 'md',
        }"
      />
    </div>

    <div class="w-full flex flex-col items-center justify-center">
      <slot />
    </div>
  </div>
</template>
