<script lang="ts" setup>
const { coords, isLocationPrecise } = useMapGeolocation();
</script>

<template>
  <Marker
    v-if="isLocationPrecise"
    :lat-lng="coords"
    :z-index-offset="-1000"
    :icon="{
      url: '/current-position-marker.png',
      size: [20, 20],
    }"
  />
</template>
