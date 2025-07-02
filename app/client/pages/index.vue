<script lang="ts" setup>
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useProblems } from "~/composables/use-problem";
import type { Problem } from "~/types/domain";
import { useMapGeolocation } from "~/composables/use-map-geolocation";

const { t } = useI18n();

useHead({
  title: "Vizo",
  meta: [
    {
      name: "description",
      content: t("head.index.description"),
    },
  ],
});

definePageMeta({
  middleware: ["auth"],
});

const map = ref<LeafletMap | null>(null);
const zoom = ref<number>(11);
const center = ref<PointExpression>([-23.5489, -46.6388]);

const isDetailsOpen = ref<boolean>(false);
const selectedProblem = ref<Problem | null>(null);

const zoomToMarker = (problem: { latitude: number; longitude: number }) => {
  map.value?.flyTo([problem.latitude, problem.longitude], 18, {
    animate: true,
    duration: 1,
  });
};

const onMarkerClick = (problem: Problem) => {
  isDetailsOpen.value = true;
  selectedProblem.value = problem;
  zoomToMarker(problem);
};

const {
  coords,
  error: geolocationError,
  isLocationPrecise,
} = useMapGeolocation();

const { problems, loading } = useProblems();
</script>

<template>
  <section class="relative size-full flex flex-row">
    <div
      class="size-full flex justify-center items-center lg:p-5"
      :class="isDetailsOpen ? 'xl:min-w-[75%]' : 'xl:w-full'"
    >
      <div v-if="loading">Loading...</div>
      <Map
        v-else
        ref="map"
        class="lg:rounded-xl lg:border border-default"
        :zoom="zoom"
        :center="center"
      >
        <Marker
          v-for="problem in problems"
          @click="onMarkerClick(problem)"
          :key="problem.id"
          :lat-lng="{
            latitude: problem.latitude,
            longitude: problem.longitude,
          }"
        />

        <CurrentPositionMarker
          v-if="isLocationPrecise && !geolocationError"
          :lat-lng="{
            latitude: coords.latitude,
            longitude: coords.longitude,
          }"
        />
      </Map>
    </div>

    <ProblemDetails
      :is-open="isDetailsOpen"
      v-if="isDetailsOpen && selectedProblem"
      @close="isDetailsOpen = false"
      :problem="selectedProblem"
      :key="selectedProblem.id"
      class="h-[40%] lg:w-60 lg:h-full"
    />
  </section>
</template>
