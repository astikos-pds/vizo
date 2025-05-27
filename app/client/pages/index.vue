<script lang="ts" setup>
import { problems } from "~/data";
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useGeolocation } from "@vueuse/core";

const map = ref<LeafletMap | null>(null);
const zoom = ref<number>(12);
const center = ref<PointExpression>([-23.5489, -46.6388]);

const isNavMenuOpen = ref<boolean>(false);

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
  <div class="flex size-full overflow-hidden bg-[#FFFFFF]">
    <div class="max-w-[20%] min-h-screen">
      <NavMenu :collapsed="!isNavMenuOpen" />
    </div>

    <div class="min-h-screen w-[100%] bg-[#FAFAFA] flex justify-center p-6">
      <Map ref="map" class="rounded-2xl" :zoom="zoom" :center="center">
        <Marker
          v-for="problem in problems"
          @click="() => onMarkerClick(problem)"
          :key="problem.id"
          :latitude="problem.latitude"
          :longitude="problem.longitude"
        />
        <LControl position="topleft">
          <UButton
            icon="i-tabler-menu-2"
            color="neutral"
            variant="outline"
            size="xl"
            class="hover:cursor-pointer size-[2.5rem] flex justify-center items-center text-[1.5rem]"
            @click="isNavMenuOpen = !isNavMenuOpen"
          />
        </LControl>
      </Map>
    </div>

    <div class="min-w-[20%] min-h-screen">
      <ProblemDetails
        v-if="isAsideOpen"
        @close="isAsideOpen = false"
        :problem-id="selectedProblem.id"
      />
    </div>
  </div>
</template>
