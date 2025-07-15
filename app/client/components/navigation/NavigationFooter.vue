<script lang="ts" setup>
import type { AvatarProps, DropdownMenuItem } from "@nuxt/ui";

const { collapsed } = defineProps<{
  collapsed: boolean;
}>();

const { logout } = useAuth();

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

const user: { name: string; avatar: AvatarProps } = reactive({
  name: "User",
  avatar: {
    src: "https://avatar.iran.liara.run/public/boy?username=Nuxt",
    icon: "i-lucide-image",
    alt: "User",
    size: "md",
  },
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
