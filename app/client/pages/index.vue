<script lang="ts" setup>
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useProblems } from "~/composables/use-problems";
import { useMapGeolocation } from "~/composables/use-map-geolocation";
import ProblemDetails from "~/components/problem/ProblemDetails.vue";
import type { Problem } from "~/types/domain/problem";
import type { LatLng } from "~/types/geolocation";

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

const { map, zoom, center } = useMap();

const zoomToMarker = (problem: { latitude: number; longitude: number }) => {
  map.value?.flyTo([problem.latitude, problem.longitude], 18, {
    animate: false,
    duration: 1,
  });
};

const overlay = useOverlay();

const onMarkerClick = (problem: Problem) => {
  const details = overlay.create(ProblemDetails, {
    props: {
      problem: problem,
    },
  });
  details.open();

  zoomToMarker(problem);
};

const {
  coords,
  error: geolocationError,
  isLocationPrecise,
} = useMapGeolocation();

const coordinates = computed<LatLng>(() => {
  return {
    latitude: coords.value.latitude,
    longitude: coords.value.longitude,
  };
});

const { getProblems } = useProblems();

const { data: problems, pending } = await getProblems();
</script>

<template>
  <section class="relative size-full flex flex-row">
    <div class="size-full flex justify-center items-center lg:p-5">
      <div v-if="pending">Loading...</div>
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
          v-model="coordinates"
        />
      </Map>
    </div>
  </section>
</template>
