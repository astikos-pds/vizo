<script lang="ts" setup>
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useGeolocation } from "@vueuse/core";
import { useProblem } from "~/composables/use-problem";
import type { Problem } from "~/types/domain";

definePageMeta({
  middleware: ["auth"],
});

const map = ref<LeafletMap | null>(null);
const zoom = ref<number>(12);
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

const { problems, loading, error } = useProblem();

const toast = useToast();
if (error.value) {
  toast.add({
    title: "Error",
    description: error.value.message,
    color: "error",
  });
}
</script>

<template>
  <div
    class="min-h-screen flex justify-center p-6 bg-neutral-50"
    :class="isAsideOpen ? 'min-w-[60%]' : 'w-full  border-r border-neutral-200'"
  >
    <div v-if="loading">Loading...</div>
    <Map v-else ref="map" class="rounded-2xl" :zoom="zoom" :center="center">
      <Marker
        v-for="problem in problems"
        @click="onMarkerClick(problem)"
        :key="problem.id"
        :lat-lng="{ latitude: problem.latitude, longitude: problem.longitude }"
      />
    </Map>
  </div>

  <div
    class="min-h-screen"
    :class="isAsideOpen ? 'w-[20%] border-x border-zinc-200' : ''"
  >
    <ProblemDetails
      v-if="isAsideOpen && selectedProblem"
      @close="isAsideOpen = false"
      :problem-id="selectedProblem.id"
      :key="selectedProblem.id"
    />
  </div>
</template>
