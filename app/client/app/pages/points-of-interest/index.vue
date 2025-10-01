<script lang="ts" setup>
import PointsOfInterestMarker from "~/components/points-of-interest/PointsOfInterestMarker.vue";
import type { Pagination } from "~/types/domain/pagination";
import type { PointOfInterest } from "~/types/domain/point-of-interest";
import type { LatLng } from "~/types/geolocation";

const { t } = useI18n();

useHead({
  title: t("head.pointsOfInterest.title"),
  meta: [
    {
      name: "description",
      content: t("head.pointsOfInterest.description"),
    },
  ],
});

definePageMeta({
  name: "Points of interest",
  middleware: ["auth"],
});

const pagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { getMyPointsOfInterest } = useMe();
const { data: page, pending } = await getMyPointsOfInterest(pagination);

const {
  items: pointsOfInterest,
  currentPage,
  totalElements,
} = usePagination(pagination, page);

const activePointsOfInterest = computed<PointOfInterest[]>(() => {
  if (!pointsOfInterest.value) return [];

  return pointsOfInterest.value.filter((p) => p.active);
});

const { map, zoom, center } = useMap();

const zoomToMarker = (marker: LatLng & { radius: number }) => {
  map.value?.flyTo(
    [marker.latitude, marker.longitude],
    17 - marker.radius / 1000,
    {
      animate: false,
      duration: 1,
    }
  );
};
</script>

<template>
  <PointsOfInterestPage>
    <template #aside>
      <div v-if="pending">{{ t("pages.pointsOfInterest.loading") }}</div>
      <div
        v-else-if="pointsOfInterest.length === 0"
        class="size-full flex justify-center items-center p-5"
      >
        <span class="text-sm">
          {{ t("pages.pointsOfInterest.noPoints") }}
          <NuxtLink to="/points-of-interest/new" class="text-primary">
            {{ t("pages.pointsOfInterest.createFirst") }}
          </NuxtLink>
        </span>
      </div>
      <div v-else class="size-full flex flex-col">
        <main class="flex-1 size-full">
          <div class="p-3 2xl:p-5 flex flex-col gap-3">
            <PointsOfInterestCard
              v-for="pointOfInterest in pointsOfInterest"
              :key="pointOfInterest.id"
              v-bind="pointOfInterest"
              @zoom-in="zoomToMarker(pointOfInterest)"
            />
          </div>
        </main>
        <footer
          v-if="page && page.totalPages > 1"
          class="p-3 flex justify-center items-center border-t border-default"
        >
          <UPagination
            v-model:page="currentPage"
            :items-per-page="pagination.size"
            :total="totalElements"
          />
        </footer>
      </div>
    </template>
    <template #main>
      <div class="size-full flex justify-center items-center">
        <div v-if="pending">{{ t("pages.pointsOfInterest.loading") }}</div>
        <Map v-else ref="map" :zoom="zoom" :center="center">
          <PointsOfInterestMarker
            v-for="pointOfInterest in activePointsOfInterest"
            :key="pointOfInterest.id"
            v-bind="pointOfInterest"
            @click="zoomToMarker(pointOfInterest)"
          />

          <CurrentPositionMarker />
        </Map>
      </div>
    </template>
  </PointsOfInterestPage>
</template>
