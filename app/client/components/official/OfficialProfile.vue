<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { Official } from "~/types/domain";

const official = defineProps<Official>();

const profileLink = `/profile/${official.id}`;

const registeredAt = Date.parse(official.createdAt);
const formattedRegisteredAt = useDateFormat(registeredAt, "DD/MM/YYYY");

const items = ref<DropdownMenuItem[]>([
  {
    label: "Promote",
    icon: "i-lucide-arrow-up",
  },
  {
    label: "Assign to department",
    icon: "i-lucide-forward",
    children: [],
  },
]);
</script>

<template>
  <UButton
    :to="profileLink"
    color="neutral"
    variant="link"
    :avatar="{
      src: avatar?.url,
      alt: name,
      size: 'xl',
    }"
    size="xl"
  >
    <div class="w-full flex justify-between item-center">
      <div class="flex flex-col">
        <span>{{ name }}</span>
        <span class="text-sm text-muted font-normal">{{ email }}</span>
      </div>

      <div class="my-auto">
        <UBadge :color="role === 'ADMIN' ? 'info' : 'neutral'" variant="subtle">
          {{ role.toLowerCase() }}
        </UBadge>
      </div>

      <div class="my-auto text-sm font-normal">
        Since {{ formattedRegisteredAt }}
      </div>

      <div v-if="role !== 'ADMIN'" class="my-auto">
        <UDropdownMenu :items="items">
          <UButton
            icon="i-lucide-ellipsis-vertical"
            color="neutral"
            variant="ghost"
          />
        </UDropdownMenu>
      </div>
    </div>
  </UButton>
</template>
