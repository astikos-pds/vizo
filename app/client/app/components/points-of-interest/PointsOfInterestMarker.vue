<script lang="ts" setup>
import type { LatLng } from "~/types/geolocation";

const coordinates = defineModel<LatLng>();

const props = defineProps<{
  radius: number;
  colorHex: string;
  latitude?: number;
  longitude?: number;
  draggable?: boolean;
}>();

const emit = defineEmits<{
  (e: "click"): void;
}>();

function updateCoordinates(newLatLng: LatLng) {
  if (!coordinates.value) return;

  coordinates.value.latitude = newLatLng.latitude;
  coordinates.value.longitude = newLatLng.longitude;
}

const latLng = computed<LatLng>(() => {
  if (coordinates.value) return coordinates.value;

  return {
    latitude: props.latitude ?? 0,
    longitude: props.longitude ?? 0,
  };
});
</script>

<template>
  <div>
    <Marker
      :lat-lng="latLng"
      :draggable="draggable"
      @click="emit('click')"
      @update:lat-lng="updateCoordinates"
    />

    <LCircle
      :lat-lng="[latLng.latitude, latLng.longitude]"
      :radius="props.radius"
      :color="props.colorHex"
      :fill-opacity="0.2"
      :opacity="1"
      :weight="1.3"
    />
  </div>
</template>
