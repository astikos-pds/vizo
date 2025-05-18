<script lang="ts" setup>
import { problems } from "~/data";
import type { Map as LeafletMap, PointExpression } from "leaflet";

const map = ref<LeafletMap | null>(null);
const zoom = ref<number>(12);
const center = ref<PointExpression>([-23.5489, -46.6388]);

const isAsideOpen = ref<boolean>(false);
const selectedProblem = ref<any>(null);

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
    <Map
      class="min-h-screen"
      :class="isAsideOpen ? 'w-2/3' : 'w-screen'"
      ref="map"
      :zoom="zoom"
      :center="center"
    >
      <Marker
        v-for="location in problems"
        @click="() => onMarkerClick(location)"
        :key="location.id"
        :latitude="location.latitude"
        :longitude="location.longitude"
      />
    </Map>

    <ProblemDetails
      class="w-1/3 min-h-screen"
      v-if="isAsideOpen"
      @close="isAsideOpen = false"
      :problem-id="selectedProblem.id"
    />
  </div>
</template>
