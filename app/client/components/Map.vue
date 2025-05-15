<script lang="ts" setup>
interface Props {
  zoom: number;
  center: number[];
}
const props = defineProps<Props>();
</script>

<template>
  <LMap
    class="min-w-screen min-h-screen"
    :zoom="props.zoom"
    :center="props.center"
    :use-global-leaflet="false"
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
