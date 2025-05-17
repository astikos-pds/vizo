<script lang="ts" setup>
import type { Map as LeafletMap } from "leaflet";

interface Props {
  zoom: number;
  center: number[];
}
const props = defineProps<Props>();

const map = ref<LeafletMap>(null);

const onReady = (mapInstance: LeafletMap) => {
  map.value = mapInstance;
};

defineExpose({
  flyTo: (targetCenter, targetZoom, options) =>
    map.value?.flyTo(targetCenter, targetZoom, options),
});
</script>

<template>
  <LMap
    class="min-w-screen min-h-screen"
    :zoom="props.zoom"
    :center="props.center"
    :use-global-leaflet="false"
    @ready="onReady"
  >
    <LTileLayer
      url="https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"
      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
      layer-type="base"
      name="OpenStreetMap"
    />
    <slot />
  </LMap>
</template>
