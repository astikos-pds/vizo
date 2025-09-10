<script lang="ts" setup>
import { useAffiliationRequestStore } from "~/stores/affiliation-request";

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
  <AffiliationsRequestStep title="Wait for approval">
    <template #description
      >Please wait until a municipal administrator approves your affiliation
      request.
      <NuxtLink to="/affiliations" class="text-primary"
        >See all your requests in the affiliations page</NuxtLink
      >.</template
    >

    <UIcon name="i-lucide-hourglass" mode="svg" class="size-15 mt-10" />
  </AffiliationsRequestStep>
</template>
