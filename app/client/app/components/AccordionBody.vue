<script lang="ts" setup>
const { t } = useI18n();

defineProps<{
  items: any[];
  emptyText?: string;
}>();
</script>

<template>
  <div class="w-full p-1 mb-6">
    <div
      v-if="items.length === 0"
      class="w-full text-sm h-24 flex justify-center items-center border border-default rounded-md"
    >
<<<<<<< HEAD
      {{ emptyText || t("components.accordionBody.emptyText") }}
=======
      {{ emptyText || t('components.accordionBody.emptyText') }}
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
    </div>
    <div
      v-else
      class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2"
    >
      <slot />
    </div>
  </div>
</template>
