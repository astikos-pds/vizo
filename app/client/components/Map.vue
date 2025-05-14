<template>
  <LMap
    class="min-w-screen min-h-screen"
    :zoom="12"
    :center="[-23.5489, -46.6388]"
    :use-global-leaflet="false"
  >
    <LTileLayer
      url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
      layer-type="base"
      name="OpenStreetMap"
    />
    <slot />
  </LMap>
</template>
