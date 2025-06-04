<template>
  <Marker
    key="user"
    :lat-lng="{
      latitude: latLng.latitude,
      longitude: latLng.longitude,
    }"
    :z-index-offset="-1000"
    :icon="{
      url: 'https://static-00.iconduck.com/assets.00/cursor-icon-2048x2048-7mjsp9p1.png',
      size: [25, 25],
    }"
  />
</template>

<script lang="ts" setup>
import type { LatLng } from "~/types/geolocation";

interface Props {
  latLng: LatLng;
}
const { latLng } = defineProps<Props>();
</script>
