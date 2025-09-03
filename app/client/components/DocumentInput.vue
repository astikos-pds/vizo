<script lang="ts" setup>
const { t } = useI18n();

const model = defineModel<string>();
</script>

<template>
  <UInput
    icon="i-lucide-id-card"
    v-model="model"
    type="text"
    :placeholder="t('register.cpfPlaceholder')"
    v-maska="'###.###.###-##'"
    class="w-full text-xl"
  />
</template>
