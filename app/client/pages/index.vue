<script lang="ts" setup>
import { locations } from "~/data";
</script>

<template>
  <Map>
    <Marker
      v-for="location in locations"
      :latitude="location.latitude"
      :longitude="location.longitude"
    ></Marker>
  </Map>
</template>
