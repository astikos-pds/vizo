<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const items = ref<DropdownMenuItem[]>([
  {
    label: "Nothing here.",
  },
]);

const department = {
  name: undefined,
  iconUrl: undefined,
};

const user = computed(() => {
  return {
    name: department.name ?? "Vizo",
    avatar: {
      src: department.iconUrl ?? "/favicon.svg",
      icon: "i-lucide-image",
      alt: "Vizo",
    },
  };
});
</script>

<template>
  <UDropdownMenu
    :items="items"
    :content="{
      align: 'start',
      side: 'top',
    }"
    :ui="{
      content: 'w-48',
      itemLeadingIcon: 'text-lg',
    }"
  >
    <div class="p-2">
      <UButton
        color="neutral"
        variant="ghost"
        :avatar="user.avatar"
        class="w-full"
        :ui="{
          base: 'p-[5px]',
        }"
      >
        {{ !collapsed ? user.name : "" }}
      </UButton>
    </div>
  </UDropdownMenu>
</template>
