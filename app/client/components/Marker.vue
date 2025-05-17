<script lang="ts" setup>
interface Icon {
  url: string;
  width: number;
  height: number;
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
      <LIcon
        :icon-url="props.icon.url"
        :icon-size="[props.icon.width, props.icon.height]"
      />
    </LMarker>
  </div>
  <div v-else>
    <LMarker
      @click="() => emit('click')"
      :lat-lng="[props.latitude, props.longitude]"
    />
  </div>
</template>
