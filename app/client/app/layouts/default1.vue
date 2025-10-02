<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

const { t } = useI18n();

const open = ref<boolean>(false);

const { user } = useLoggedInUserStore();

const commomItems = computed<NavigationMenuItem[]>(() => {
  return [
    {
      label: t("components.navigation.home"),
      icon: "i-lucide-house",
      to: "/",
      onSelect: () => (open.value = false),
    },
    {
      label: t("components.navigation.notifications"),
      icon: "i-lucide-bell",
      to: "/notifications",
      onSelect: () => (open.value = false),
    },
    {
      label: t("components.navigation.reports"),
      icon: "i-lucide-message-square-warning",
      to: "/reports/new",
      onSelect: () => (open.value = false),
      children: [
        {
          label: t("components.navigation.newProblem"),
          icon: "i-lucide-plus",
          to: "/reports/new",
          onSelect: () => (open.value = false),
        },
        {
          label: t("components.navigation.history"),
          icon: "i-lucide-history",
          to: "/reports",
          onSelect: () => (open.value = false),
        },
      ],
    },
    {
      label: t("components.navigation.pointsOfInterest"),
      icon: "i-lucide-map-pin",
      to: "/points-of-interest",
      onSelect: () => (open.value = false),
      children: [
        {
          label: t("components.navigation.viewAll"),
          icon: "i-lucide-map",
          to: "/points-of-interest",
          onSelect: () => (open.value = false),
        },
        {
          label: t("components.navigation.createNew"),
          icon: "i-lucide-map-pin-plus",
          to: "/points-of-interest/new",
          onSelect: () => (open.value = false),
        },
      ],
    },
    {
      label: t("components.navigation.affiliations"),
      icon: "i-lucide-archive",
      to: "/affiliations",
      onSelect: () => (open.value = false),
      children: [
        {
          label: t("components.navigation.viewAll"),
          icon: "i-lucide-building",
          to: "/affiliations",
          onSelect: () => (open.value = false),
        },
        {
          label: t("components.navigation.requestNew"),
          icon: "i-lucide-git-pull-request",
          to: "/affiliations/request",
          onSelect: () => (open.value = false),
        },
      ],
    },
    {
      label: t("components.navigation.settings"),
      icon: "i-lucide-settings",
      to: "/settings",
      onSelect: () => (open.value = false),
    },
  ];
});

const groups = computed(() => [
  {
    id: "links",
    label: "Go to",
    items: commomItems.value.flat(),
  },
]);
</script>

<template>
  <UDashboardGroup unit="rem">
    <UDashboardSidebar
      id="default"
      v-model:open="open"
      collapsible
      resizable
      class="bg-elevated/25"
      :ui="{ footer: 'lg:border-t lg:border-default' }"
    >
      <template #header="{ collapsed }">
        <NavigationHeader :collapsed="collapsed" />
      </template>

      <template #default="{ collapsed }">
        <UDashboardSearchButton
          :collapsed="collapsed"
          size="xl"
          class="bg-transparent ring-default"
        />

        <NavigationBody :collapsed="collapsed" v-model:open="open" />
      </template>

      <template #footer="{ collapsed }">
        <NavigationUserProfile
          v-if="user"
          :user="user"
          :collapsed="collapsed"
        />
      </template>
    </UDashboardSidebar>

    <UDashboardSearch :groups="groups" />

    <slot />
  </UDashboardGroup>
</template>
