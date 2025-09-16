<script lang="ts" setup>
import type { PointExpression } from "leaflet";
import type { LatLng } from "~/types/geolocation";

interface Icon {
  url: string;
  size: PointExpression;
}

interface Props {
  latLng?: LatLng;
  icon?: Icon;
  draggable?: boolean;
  zIndexOffset?: number;
}

const props = defineProps<Props>();

const coordinates = defineModel<LatLng>();

const emit = defineEmits<{
  (e: "click"): void;
  (e: "update:latLng", newLatLng: LatLng): void;
}>();

const hasIcon = computed(() => {
  return props.icon && props.icon.url;
});

const latLng = computed(() =>
  coordinates.value
    ? coordinates.value
    : props.latLng
    ? props.latLng
    : CITY_CENTER
);

function onMove(event: any) {
  const newLatLng: { lat: number; lng: number } = event.target.getLatLng();

  if (coordinates.value) {
    coordinates.value.latitude = newLatLng.lat;
    coordinates.value.longitude = newLatLng.lng;
  }

  emit("update:latLng", { latitude: newLatLng.lat, longitude: newLatLng.lng });
}
</script>

<template>
  <div v-if="hasIcon">
    <LMarker
      @click="() => emit('click')"
      @moveend="onMove"
      :lat-lng="[latLng.latitude, latLng.longitude]"
      :draggable="props.draggable"
      :z-index-offset="props.zIndexOffset"
    >
      <LIcon :icon-url="props.icon?.url" :icon-size="props.icon?.size" />
    </LMarker>
  </div>
  <div v-else>
    <LMarker
      @click="() => emit('click')"
      @moveend="onMove"
      :lat-lng="[latLng.latitude, latLng.longitude]"
      :draggable="props.draggable"
      :z-index-offset="props.zIndexOffset"
    />
  </div>
</template>
