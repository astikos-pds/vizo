<script lang="ts" setup>
interface Icon {
  url: string;
  width: number;
  height: number;
}

interface Props {
  latitude: number;
  longitude: number;
  icon: Icon | undefined;
}

const props = defineProps<Props>();
</script>

<template>
  <div v-if="props.icon">
    <LMarker :lat-lng="[props.latitude, props.longitude]">
      <LIcon
        :icon-url="props.icon?.url"
        :icon-size="[props.icon?.width, props.icon?.height]"
      />
    </LMarker>
  </div>
  <div v-else>
    <LMarker :lat-lng="[props.latitude, props.longitude]"></LMarker>
  </div>
</template>
