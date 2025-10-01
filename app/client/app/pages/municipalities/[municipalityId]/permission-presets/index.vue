<script lang="ts" setup>
definePageMeta({
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

await navigateTo(`/municipalities/${municipalityId}/permission-presets/new`);
</script>

<template></template>
