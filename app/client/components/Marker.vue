<script lang="ts" setup>
import type { PointExpression } from "leaflet";

interface Icon {
  url: string;
  size: PointExpression;
}

interface Props {
  latitude: number;
  longitude: number;
  icon?: Icon;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (e: "click"): void;
}>();

const hasIcon = computed(() => {
  return props.icon && props.icon.url;
});
</script>

<template>
  <div v-if="hasIcon">
    <LMarker
      @click="() => emit('click')"
      :lat-lng="[props.latitude, props.longitude]"
    >
      <LIcon :icon-url="props.icon?.url" :icon-size="props.icon?.size" />
    </LMarker>
  </div>
  <div v-else>
    <LMarker
      @click="() => emit('click')"
      :lat-lng="[props.latitude, props.longitude]"
    />
  </div>
</template>
