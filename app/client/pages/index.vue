<script lang="ts" setup>
import { locations } from "~/data";

const map = ref(null);
const zoom = ref(12);
const center = ref([-23.5489, -46.6388]);

const zoomToMarker = (location: { latitude: number; longitude: number }) => {
  map.value?.flyTo([location.latitude, location.longitude], 18, {
    animate: true,
    duration: 1,
  });
};
</script>

<template>
  <Map ref="map" :zoom="zoom" :center="center">
    <Marker
      v-for="location in locations"
      @click="() => zoomToMarker(location)"
      :key="location.name"
      :latitude="location.latitude"
      :longitude="location.longitude"
      :icon="{
        width: 20,
        height: 20,
      }"
    />
    <LMarker :lat-lng="center"></LMarker>
  </Map>
</template>
