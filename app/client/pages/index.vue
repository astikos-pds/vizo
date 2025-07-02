<script lang="ts" setup>
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useProblems } from "~/composables/use-problem";
import type { Problem } from "~/types/domain";
import { useMapGeolocation } from "~/composables/use-map-geolocation";
import ProblemDetails from "~/components/problem/ProblemDetails.vue";

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

const zoomToMarker = (problem: { latitude: number; longitude: number }) => {
  map.value?.flyTo([problem.latitude, problem.longitude], 18, {
    animate: true,
    duration: 1,
  });
};

const overlay = useOverlay();

const onMarkerClick = (problem: Problem) => {
  const problemDetails = overlay.create(ProblemDetails, {
    props: {
      problem: problem,
    },
  });
  problemDetails.open();

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
    <div class="size-full flex justify-center items-center lg:p-5">
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
  </section>
</template>
