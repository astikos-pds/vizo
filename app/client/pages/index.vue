<script lang="ts" setup>
import { locations } from "~/data";

const zoom = ref(12);
const center = ref([-23.5489, -46.6388]);

const handleZoom = (location: { latitude: number; longitude: number }) => {
  console.log("safasdfs");
  zoom.value = 5;
  center.value = [location.latitude, location.longitude];
};
</script>

<template>
  <Map :zoom="zoom" :center="center">
    <Marker
      v-for="location in locations"
      @click="handleZoom"
      :latitude="location.latitude"
      :longitude="location.longitude"
      :icon="{
        url: 'https://static.thenounproject.com/png/335079-200.png',
        width: 20,
        height: 20,
      }"
    />
  </Map>
</template>
