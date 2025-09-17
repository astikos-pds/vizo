<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { PointOfInterest } from "~/types/domain/point-of-interest";

const { t, locale } = useI18n();

const pointOfInterest = defineProps<PointOfInterest>();

const emit = defineEmits(["zoomIn"]);

const formattedRadius =
  pointOfInterest.radius >= 1000
    ? `${pointOfInterest.radius / 1000} km`
    : `${pointOfInterest.radius} m`;

const { deletePointOfInterest } = usePointsOfInterest();

async function onDelete(id: PointOfInterest["id"]) {
  await deletePointOfInterest(id);

  await refreshNuxtData();
}

const actions = ref<DropdownMenuItem[]>([
  {
    label: t('components.pointsOfInterest.viewOnMap'),
    icon: "i-lucide-eye",
    onSelect: () => emit("zoomIn"),
  },
  {
    label: t('components.pointsOfInterest.edit'),
    icon: "i-lucide-pencil",
    to: `/points-of-interest/${pointOfInterest.id}/edit`,
  },
  {
    label: t('components.pointsOfInterest.delete'),
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
    <div class="size-full flex gap-3 relative items-center">
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
            {{ pointOfInterest.active ? t('components.pointsOfInterest.active') : t('components.pointsOfInterest.inactive') }}
          </UBadge>
        </header>
        <main class="text-xs 2xl:text-sm flex flex-col">
          <span>{{ t('components.pointsOfInterest.radius') }}: {{ formattedRadius }}</span>
          <span
            >{{ t('components.pointsOfInterest.createdAt') }}:
            {{
              pointOfInterest.createdAt.toLocaleDateString(locale, {
                dateStyle: "full",
              })
            }}</span
          >
        </main>
      </div>
      <UDropdownMenu :items="actions">
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
