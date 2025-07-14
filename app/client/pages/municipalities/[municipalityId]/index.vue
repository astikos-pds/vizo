<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { getMunicipalityByIdUseCase } from "~/services/municipality";
import type { Municipality } from "~/types/domain";

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const { params } = useRoute();
const municipalityId = params.municipalityId as string;

const { isAdmin } = useUserStore();
const { t } = useI18n();

const {
  data: municipality,
  error,
  pending,
} = await useAsyncData<Municipality>("municipality", () =>
  getMunicipalityByIdUseCase({ id: municipalityId })
);

const items = computed<NavigationMenuItem[]>(() => {
  const baseItems: NavigationMenuItem[] = [
    {
      label: t("municipalitiesId.navigation.home"),
      icon: "i-lucide-home",
      to: `/municipalities/${municipalityId}`,
    },
    {
      label: t("municipalitiesId.navigation.departments"),
      icon: "i-lucide-box",
      to: `/municipalities/${municipalityId}/departments`,
    },
    {
      label: t("municipalitiesId.navigation.members"),
      icon: "i-lucide-users",
      to: `/municipalities/${municipalityId}/members`,
    },
  ];

  if (isAdmin) {
    baseItems.push({
      label: t("municipalitiesId.navigation.affiliations"),
      icon: "i-lucide-contact",
      to: `/municipalities/${municipalityId}/affiliations`,
    });
  }

  return baseItems;
});

const options = computed(() => {
  const name = municipality.value?.name ?? "";

  const allOptions = [
    {
      to: `/municipalities/${municipalityId}/departments`,
      icon: "i-lucide-box",
      title: t("municipalitiesId.options.departments.title"),
      description: t("municipalitiesId.options.departments.description", {
        name,
      }),
      adminOnly: false,
    },
    {
      to: `/municipalities/${municipalityId}/members`,
      icon: "i-lucide-users",
      title: t("municipalitiesId.options.members.title"),
      description: t("municipalitiesId.options.members.description", { name }),
      adminOnly: false,
    },
    {
      to: `/municipalities/${municipalityId}/affiliations`,
      icon: "i-lucide-contact",
      title: t("municipalitiesId.options.affiliations.title"),
      description: t("municipalitiesId.options.affiliations.description", {
        name,
      }),
      adminOnly: true,
    },
  ];

  return allOptions.filter((opt) => !opt.adminOnly || isAdmin);
});
</script>

<template>
  <div v-if="pending" class="mt-8 text-center text-gray-500">
    {{ t("municipalitiesId.loading.municipality") }}
  </div>

  <div v-else-if="error || !municipality" class="mt-8 text-center">
    {{ t("municipalitiesId.error.notFound", { id: municipalityId }) }}
  </div>

  <div v-else class="w-full flex flex-col justify-center items-center">
    <UNavigationMenu
      :items
      aria-label="Main municipality navigation"
      class="w-full flex justify-center border-b border-default"
    />

    <OfficialPage
      :title="municipality.name"
      :description="t('municipalitiesId.municipality.description')"
    >
      <div class="size-full grid grid-cols-1 sm:grid-cols-2 gap-2 lg:gap-3">
        <MunicipalityOption
          v-for="option in options"
          :key="option.to"
          :to="option.to"
          :icon="option.icon"
          :title="option.title"
          :description="option.description"
        />
      </div>
    </OfficialPage>
  </div>
</template>
