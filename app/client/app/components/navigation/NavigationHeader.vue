<script lang="ts" setup>
const { collapsed } = defineProps<{
  collapsed?: boolean;
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
  <section class="w-full flex justify-center items-center flex-col gap-2">
    <UButton
      color="neutral"
      variant="ghost"
      :avatar="vizo.avatar"
      size="xl"
      block
      :square="collapsed"
      class="justify-start p-1"
    >
      {{ !collapsed ? vizo.name : "" }}
    </UButton>
  </section>
</template>
