<script lang="ts" setup>
import type { AvatarProps, DropdownMenuItem } from "@nuxt/ui";

const { currentAffiliation, affiliations } = useLoggedInUserStore();

const items = computed<DropdownMenuItem[]>(() =>
  affiliations.map((a) => {
    return {
      label: a.municipality.name,
      avatar: {
        src: a.municipality.iconUrl?.toString(),
        icon: "i-lucide-image",
        alt: a.municipality.name,
      },
      to: `/municipalities/${a.municipality.id}`,
    };
  })
);

const avatar = computed<AvatarProps | undefined>(() => {
  if (!currentAffiliation) return undefined;

  return {
    src: currentAffiliation.municipality.iconUrl?.toString(),
    icon: "i-lucide-image",
    alt: currentAffiliation.municipality.name,
  };
});
</script>

<template>
  <UDropdownMenu
    :items="items"
    :ui="{
      content: 'w-48',
    }"
  >
    <UButton color="neutral" variant="ghost" :avatar="avatar">{{
      currentAffiliation?.municipality.name
    }}</UButton>
  </UDropdownMenu>
</template>
