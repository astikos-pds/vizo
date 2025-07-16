<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: "Profile",
      icon: "i-lucide-user",
    },
  ],
  [
    {
      label: "Exit",
      icon: "i-lucide-log-out",
      to: "/logout",
    },
  ],
]);

const { user } = useUserStore();
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
    <div>
      <UButton
        color="neutral"
        variant="ghost"
        :avatar="{
          src: user.profile.avatar?.url,
          alt: user.profile.name,
          size: 'md',
        }"
        class="w-full"
        :ui="{
          base: 'p-[5px]',
        }"
      >
        {{ !collapsed ? user.profile.name : "" }}
      </UButton>
    </div>
  </UDropdownMenu>
</template>
