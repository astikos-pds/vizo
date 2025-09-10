<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const vizo = {
  name: "Vizo",
  avatar: {
    src: "/favicon.svg",
    icon: "i-lucide-image",
    alt: "Vizo",
  },
};
</script>

<template>
  <section class="px-2 w-full flex flex-col gap-2">
    <UButton
      color="neutral"
      variant="ghost"
      :avatar="vizo.avatar"
      size="xl"
      class="w-full"
    >
      {{ !collapsed ? vizo.name : "" }}
    </UButton>
  </section>
</template>
