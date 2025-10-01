<script lang="ts" setup>
definePageMeta({
  middleware: ["auth", "affiliated"],
});

const { currentAffiliation } = useLoggedInUserStore();

if (!currentAffiliation) await navigateTo("/affiliations");
else
  await navigateTo(
    `/municipalities/${currentAffiliation.municipality.id}/departments`
  );
</script>

<template></template>
