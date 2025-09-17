<script lang="ts" setup>
import { useAffiliationRequestStore } from "~/stores/affiliation-request";

const { t } = useI18n();

const stepper = useSteps();
const store = useAffiliationRequestStore();

onMounted(() => {
  store.reset();
});

onBeforeUnmount(() => {
  stepper.goToStart();
});
</script>

<template>
  <AffiliationsRequestStep :title="t('components.affiliations.waitForApproval')">
    <template #description
      >{{ t('components.affiliations.waitDescription') }}
      <NuxtLink to="/affiliations" class="text-primary">{{
        t('components.affiliations.seeAllRequests')
      }}</NuxtLink>.</template
    >

    <UIcon name="i-lucide-hourglass" mode="svg" class="size-15 mt-10" />
  </AffiliationsRequestStep>
</template>
