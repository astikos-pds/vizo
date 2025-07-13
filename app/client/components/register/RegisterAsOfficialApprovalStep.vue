<script lang="ts" setup>
const { t } = useI18n();
</script>

<template>
  <RegisterAsOfficialStep :title="t('registerOfficial.approvalStep.title')">
    <template #description>{{
      t("registerOfficial.approvalStep.description")
    }}</template>

    <UIcon name="i-lucide-hourglass" mode="svg" class="size-15" />
  </RegisterAsOfficialStep>
</template>
