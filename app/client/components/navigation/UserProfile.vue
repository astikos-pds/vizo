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

const { user } = useLoggedInUserStore();
</script>

<template>
  <UDropdownMenu
    v-if="user"
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
          src: user.avatarUrl?.toString(),
          alt: user.name,
          size: 'md',
        }"
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
