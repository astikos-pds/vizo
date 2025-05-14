<script lang="ts" setup>
import { locations } from "~/data";
</script>

<template>
  <Map>
    <LMarker
      v-for="location in locations"
      :lat-lng="[location.latitude, location.longitude]"
    >
    </LMarker>
    <!-- 
      <LIcon
        icon-url="https://static.thenounproject.com/png/888711-200.png"
        :icon-size="[50, 50]"
      /> 
    -->
  </Map>
</template>
