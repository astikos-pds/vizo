<script lang="ts" setup>
import { problems } from "~/data";
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useGeolocation } from "@vueuse/core";

const map = ref<LeafletMap | null>(null);
const zoom = ref<number>(12);
const center = ref<PointExpression>([-23.5489, -46.6388]);

const isAsideOpen = ref<boolean>(false);
const selectedProblem = ref<any>(null);

const { coords, locatedAt, error } = useGeolocation();

const zoomToMarker = (problem: { latitude: number; longitude: number }) => {
  map.value?.flyTo([problem.latitude, problem.longitude], 18, {
    animate: true,
    duration: 1,
  });
};

const onMarkerClick = (problem: { latitude: number; longitude: number }) => {
  isAsideOpen.value = true;
  selectedProblem.value = problem;
  zoomToMarker(problem);
};
</script>

<template>
  <div class="flex min-w-screen min-h-screen overflow-hidden">
    <NavMenu />

    <div class="min-h-screen w-[60%]">
      <Map ref="map" :zoom="zoom" :center="center">
        <Marker
          v-for="problem in problems"
          @click="() => onMarkerClick(problem)"
          :key="problem.id"
          :latitude="problem.latitude"
          :longitude="problem.longitude"
        />
        <LControl position="bottomleft">
          <ReportButton />
        </LControl>
      </Map>
    </div>

    <ProblemDetails
      class="w-[20%] min-h-screen"
      v-if="isAsideOpen"
      @close="isAsideOpen = false"
      :problem-id="selectedProblem.id"
    />
  </div>
</template>
