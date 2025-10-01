<script lang="ts" setup>
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
  <section class="w-full flex flex-col gap-2" :class="{ 'px-2': !collapsed }">
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
