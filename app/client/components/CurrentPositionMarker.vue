<script lang="ts" setup>
import type { LatLng } from "~/types/geolocation";

const model = defineModel<LatLng>({ required: true });
</script>

<template>
  <Marker
    key="user"
    :lat-lng="model"
    :z-index-offset="-1000"
    :icon="{
      url: '/current-position-marker.png',
      size: [20, 20],
    }"
  />
</template>
