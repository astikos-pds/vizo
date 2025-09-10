<script lang="ts" setup>
import PointsOfInterestMarker from "~/components/points-of-interest/PointsOfInterestMarker.vue";
import type { Pagination } from "~/types/domain/pagination";
import type { PointOfInterest } from "~/types/domain/point-of-interest";
import type { LatLng } from "~/types/geolocation";

useHead({
  title: "Vizo | Points of interest",
  meta: [
    {
      name: "description",
      content: "View all yours points of interest in the city.",
    },
  ],
});

definePageMeta({
  middleware: ["auth"],
});

const pagination = reactive<Pagination>({
  page: 0,
  size: 1,
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

const { coords, isLocationPrecise } = useMapGeolocation();

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
  <PointsOfInterestPage title="Points of interest">
    <template #aside>
      <div v-if="pending">Loading</div>
      <div
        v-else-if="pointsOfInterest.length === 0"
        class="size-full flex justify-center items-center p-5"
      >
        <span class="text-sm"
          >No points of interest found.
          <NuxtLink to="/points-of-interest/new" class="text-primary"
            >Create your first one here.</NuxtLink
          ></span
        >
      </div>
      <div v-else class="size-full flex flex-col">
        <main class="flex-1">
          <div class="p-3 flex flex-col gap-3">
            <PointsOfInterestCard
              v-if="pointsOfInterest.length > 0"
              v-for="pointOfInterest in pointsOfInterest"
              :key="pointOfInterest.id"
              v-bind="pointOfInterest"
              @zoom-in="zoomToMarker(pointOfInterest)"
            />
          </div>
        </main>
        <footer
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
        <div v-if="pending">Loading...</div>
        <Map v-else ref="map" :zoom="zoom" :center="center">
          <PointsOfInterestMarker
            v-if="activePointsOfInterest.length > 0"
            :key="pointOfInterest.id"
            v-for="pointOfInterest in activePointsOfInterest"
            v-bind="pointOfInterest"
            @click="zoomToMarker(pointOfInterest)"
          />

          <CurrentPositionMarker v-if="isLocationPrecise" v-model="coords" />
        </Map>
      </div>
    </template>
  </PointsOfInterestPage>
</template>
