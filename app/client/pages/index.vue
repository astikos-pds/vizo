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

const isAsideOpen = ref<boolean>(false);
const selectedProblem = ref<Problem | null>(null);

const zoomToMarker = (problem: { latitude: number; longitude: number }) => {
  map.value?.flyTo([problem.latitude, problem.longitude], 18, {
    animate: true,
    duration: 1,
  });
};

const onMarkerClick = (problem: Problem) => {
  isAsideOpen.value = true;
  selectedProblem.value = problem;
  zoomToMarker(problem);
};

const {
  coords,
  error: geolocationError,
  isLocationPrecise,
} = useMapGeolocation();

const geolocationErrorMessage = computed(() => {
  if (geolocationError.value?.PERMISSION_DENIED)
    return t("reportProblem.localization.error.permissionDenied");
  if (geolocationError.value?.POSITION_UNAVAILABLE)
    return t("reportProblem.localization.error.positionUnavailable");
  if (geolocationError.value?.TIMEOUT)
    return t("reportProblem.localization.error.timeout");
});

const { problems, loading } = useProblems();
</script>

<template>
  <section class="relative min-h-screen w-full flex flex-col xl:flex-row">
    <div
      class="min-h-screen flex justify-center lg:p-5 bg-neutral-100 dark:bg-neutral-800"
      :class="
        isAsideOpen
          ? 'xl:min-w-[75%]'
          : 'xl:w-full border-r border-neutral-200 dark:border-neutral-800'
      "
    >
      <div v-if="loading">Loading...</div>
      <Map
        v-else
        v-if="!geolocationError"
        ref="map"
        class="rounded-2xl border border-neutral-200"
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
      <p v-if="geolocationError" class="text-error mb-2">
        {{ geolocationErrorMessage }}
      </p>
    </div>

    <div
      class="absolute bottom-0 bg-neutral-50 z-10000000000 xl:min-h-screen xl:relative"
      :class="
        isAsideOpen
          ? 'w-full h-[20rem] border-y xl:w-[25%] xl:border-x border-neutral-200 dark:border-neutral-800'
          : ''
      "
    >
      <ProblemDetails
        v-if="isAsideOpen && selectedProblem"
        @close="isAsideOpen = false"
        :problem="selectedProblem"
        :key="selectedProblem.id"
      />
    </div>
  </section>
</template>
