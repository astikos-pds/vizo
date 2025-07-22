<script lang="ts" setup>
defineProps<{
  src: string;
  alt: string;
}>();
</script>

<template>
  <UModal>
    <UButton variant="link">
      <img
        :src="src"
        :alt="alt"
        class="w-full h-full object-cover rounded-md"
      />
    </UButton>

    <template #content>
      <img
        :src="src"
        :alt="alt"
        class="w-full h-full object-cover rounded-md"
      />
    </template>
  </UModal>
</template>
