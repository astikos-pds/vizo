<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { PointOfInterest } from "~/types/domain/point-of-interest";

const { locale } = useI18n();

const pointOfInterest = defineProps<PointOfInterest>();

const formattedRadius =
  pointOfInterest.radius >= 1000
    ? `${pointOfInterest.radius / 1000} km`
    : `${pointOfInterest.radius} m`;

const { deletePointOfInterest } = usePointsOfInterest();

async function onDelete(id: PointOfInterest["id"]) {
  await deletePointOfInterest(id);

  await refreshNuxtData("my-points-of-interest");
}

const actions = ref<DropdownMenuItem[]>([
  {
    label: "Edit",
    icon: "i-lucide-pencil",
    to: `/points-of-interest/${pointOfInterest.id}/edit`,
  },
  {
    label: "Delete",
    icon: "i-lucide-trash",
    color: "error",
    onSelect: () => onDelete(pointOfInterest.id),
  },
]);
</script>

<template>
  <UCard
    :variant="pointOfInterest.active ? 'outline' : 'subtle'"
    :class="{ 'opacity-80': !pointOfInterest.active }"
  >
    <div class="size-full flex gap-3 relative">
      <div class="min-h-full flex justify-center items-center">
        <div
          class="rounded-full size-13 flex items-center justify-center"
          :style="{ backgroundColor: pointOfInterest.colorHex }"
        >
          <UIcon name="i-lucide-map-pin" size="25" class="text-neutral-100" />
        </div>
      </div>
      <div class="flex-1 flex flex-col gap-1">
        <header class="flex gap-1.5 2xl:gap-2 items-center">
          <span class="font-medium text-md">{{ pointOfInterest.name }}</span>

          <UBadge
            size="sm"
            variant="subtle"
            :color="pointOfInterest.active ? 'success' : 'neutral'"
          >
            {{ pointOfInterest.active ? "Active" : "Inactive" }}
          </UBadge>
        </header>
        <main class="text-xs 2xl:text-sm flex flex-col">
          <span>Radius: {{ formattedRadius }}</span>
          <span
            >Created at:
            {{
              pointOfInterest.createdAt.toLocaleDateString(locale, {
                dateStyle: "full",
              })
            }}</span
          >
        </main>
      </div>
      <UDropdownMenu class="absolute top-0 right-0" :items="actions">
        <UButton
          variant="ghost"
          color="neutral"
          icon="i-lucide-ellipsis"
          size="xl"
        />
      </UDropdownMenu>
    </div>
  </UCard>
</template>
