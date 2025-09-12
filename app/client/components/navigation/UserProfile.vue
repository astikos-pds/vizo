<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { User } from "~/types/domain/user";

const { user, collapsed } = defineProps<{
  user: User;
  collapsed: boolean;
}>();

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: "Profile",
      icon: "i-lucide-user",
      to: `/users/${user.id}`,
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
</script>

<template>
  <UDropdownMenu
    :items="items"
    :content="{
      align: 'center',
      side: 'top',
    }"
    :ui="{
      content: 'w-48',
      itemLeadingIcon: 'text-lg',
    }"
    class="w-full"
  >
    <UButton
      color="neutral"
      variant="ghost"
      :avatar="{
        src: user.avatarUrl?.toString(),
        alt: user.name,
        size: 'md',
      }"
      size="lg"
      :label="collapsed ? undefined : user.name"
    />
  </UDropdownMenu>
</template>
