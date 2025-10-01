<script lang="ts" setup>
import type {
  Map as LeafletMap,
  LatLngExpression,
  PointExpression,
} from "leaflet";
import type { LatLng } from "~/types/geolocation";

interface Props {
  zoom: number;
  center: LatLng;
}
const props = defineProps<Props>();

const map = ref<LeafletMap | null>(null);

const onReady = (mapInstance: LeafletMap) => {
  map.value = mapInstance;
};

defineExpose({
  flyTo: (targetCenter: LatLngExpression, targetZoom: number, options: any) =>
    map.value?.flyTo(targetCenter, targetZoom, options),
});

const colorMode = useColorMode();

const tileLayerUrl = computed(() =>
  colorMode.value === "dark"
    ? "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
    : "https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"
);
</script>

<template>
  <LMap
    :zoom="props.zoom"
    :center="[props.center.latitude, props.center.longitude]"
    :use-global-leaflet="false"
    world-copy-jump
    @ready="onReady"
  >
    <LTileLayer
      :url="tileLayerUrl"
      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
      layer-type="base"
      name="OpenStreetMap"
    />
    <slot />
  </LMap>
</template>
