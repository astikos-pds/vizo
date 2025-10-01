<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { User } from "~/types/domain/user";

const { t } = useI18n();

const { user, collapsed } = defineProps<{
  user: User;
  collapsed: boolean;
}>();

const items = ref<DropdownMenuItem[][]>([
  [
    {
      label: t("components.navigation.profile"),
      icon: "i-lucide-user",
      to: `/users/${user.id}`,
    },
  ],
  [
    {
      label: t("components.navigation.exit"),
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
