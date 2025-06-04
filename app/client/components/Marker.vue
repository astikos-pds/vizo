<script lang="ts" setup>
import type { PointExpression } from "leaflet";
import type { LatLng } from "~/types/geolocation";

interface Icon {
  url: string;
  size: PointExpression;
}

interface Props {
  latLng: LatLng;
  icon?: Icon;
  draggable?: boolean;
  zIndexOffset?: number;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (e: "click"): void;
  (e: "update:latLng", newLatLng: LatLng): void;
}>();

const hasIcon = computed(() => {
  return props.icon && props.icon.url;
});

function onMove(event: any) {
  const newLatLng: { lat: number; lng: number } = event.target.getLatLng();
  emit("update:latLng", { latitude: newLatLng.lat, longitude: newLatLng.lng });
}
</script>

<template>
  <div v-if="hasIcon">
    <LMarker
      @click="() => emit('click')"
      @moveend="onMove"
      :lat-lng="[props.latLng.latitude, props.latLng.longitude]"
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
      :lat-lng="[props.latLng.latitude, props.latLng.longitude]"
      :draggable="props.draggable"
      :z-index-offset="props.zIndexOffset"
    />
  </div>
</template>
