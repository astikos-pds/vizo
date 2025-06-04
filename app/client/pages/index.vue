<script lang="ts" setup>
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useGeolocation } from "@vueuse/core";
import { useProblems } from "~/composables/use-problem";
import type { Problem } from "~/types/domain";

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

const { coords } = useGeolocation();

const { problems, loading, error } = useProblems();

const toast = useToast();
if (error.value) {
  toast.add({
    title: "Error",
    description: t(
      `toast.error.description.${error.value.statusCode}`,
      t("toast.error.description.500")
    ),
    color: "error",
  });
}
</script>

<template>
  <section class="relative min-h-screen w-full flex flex-col xl:flex-row">
    <div
      class="min-h-screen flex justify-center lg:p-5 bg-neutral-50"
      :class="
        isAsideOpen ? 'xl:min-w-[75%]' : 'xl:w-full border-r border-neutral-200'
      "
    >
      <div v-if="loading">Loading...</div>
      <Map v-else ref="map" class="rounded-2xl" :zoom="zoom" :center="center">
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
          :lat-lng="{
            latitude: coords.latitude,
            longitude: coords.longitude,
          }"
        />
      </Map>
    </div>

    <div
      class="absolute bottom-0 bg-[#FFFFFF] z-10000000000 xl:min-h-screen xl:relative"
      :class="
        isAsideOpen
          ? 'w-full h-[20rem] border-y xl:w-[25%] xl:border-x border-zinc-200'
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
