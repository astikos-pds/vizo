<script lang="ts" setup>
const route = useRoute();
const municipalityId = route.params.municipalityId as string;

await navigateTo(`/municipalities/${municipalityId}/permission-presets/new`);
</script>

<template></template>
