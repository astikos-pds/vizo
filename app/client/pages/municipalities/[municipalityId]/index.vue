<script lang="ts" setup>
import { municipalityRepository } from "~/repositories/municipality-repository";

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { isAdmin } = useUserStore();
const { t } = useI18n();

const {
  data: municipality,
  error,
  pending,
} = await municipalityRepository.getById(municipalityId, {
  key: `municipality-${municipalityId}`,
});

useHead({
  title: `Vizo | ${municipality.value?.name}`,
  meta: [
    {
      name: "description",
      content: "Find all officials affiliated to municipality",
    },
  ],
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
      to: `/municipalities/${municipalityId}/officials`,
      icon: "i-lucide-users",
      title: t("municipalitiesId.options.officials.title"),
      description: t("municipalitiesId.options.officials.description", {
        name,
      }),
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
  <div v-if="pending" class="mt-8 text-center">
    {{ t("municipalitiesId.loading.municipality") }}
  </div>

  <div v-else-if="error || !municipality" class="mt-8 text-center">
    {{ t("municipalitiesId.error.notFound", { id: municipalityId }) }}
  </div>

  <OfficialPage
    v-else
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
</template>
