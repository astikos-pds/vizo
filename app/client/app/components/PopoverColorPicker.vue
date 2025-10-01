<script lang="ts" setup>
const { t } = useI18n();

const colorHex = defineModel<string>();

const chip = computed(() => ({ backgroundColor: colorHex.value }));
</script>

<template>
  <UPopover>
    <UButton
      :label="t('components.popoverColorPicker.chooseColor')"
      color="neutral"
      variant="outline"
      class="w-full"
    >
      <template #leading>
        <span :style="chip" class="size-3 rounded-full" />
      </template>
    </UButton>

    <template #content>
      <UColorPicker v-model="colorHex" class="p-2" />
    </template>
  </UPopover>
</template>
