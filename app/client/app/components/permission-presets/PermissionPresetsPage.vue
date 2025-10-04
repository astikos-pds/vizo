<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";

defineProps<{
  title: string;
}>();

const { t } = useI18n();

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();
const { getPermissionPresetsInMunicipality } = usePermissionPresets();

const { data: permissionPresets } = await getPermissionPresetsInMunicipality(
  municipalityId
);

const items = computed<NavigationMenuItem[]>(() => {
  if (!currentAffiliation || !permissionPresets.value) return [];

  const base = permissionPresets.value.map((p) => {
    return {
      label: p.name,
      to: `/municipalities/${p.municipality.id}/permission-presets/${p.id}`,
    };
  });

  return [
    ...base,
    {
      label: t("components.permissionPresets.createNew"),
      icon: "i-lucide-badge-plus",
      to: `/municipalities/${currentAffiliation.municipality.id}/permission-presets/new`,
    },
  ];
});
</script>

<template>
  <CommonPage
    v-if="currentAffiliation && currentAffiliation.isAdmin"
    :title="title"
    :toolbar-items="items"
  >
    <main class="size-full flex flex-col items-center">
      <div
        class="flex-1 w-[90%] lg:w-[80%] 2xl:w-[75%] p-3 my-8 gap-3 flex flex-col items-center"
      >
        <div class="w-full flex flex-col items-center">
          <slot />
        </div>
      </div>
    </main>
  </CommonPage>
</template>
