<script lang="ts" setup>
definePageMeta({
  middleware: ["auth", "affiliated"],
});

const route = useRoute();

await navigateTo(
  `/municipalities/${route.params.municipalityId as string}/departments`
);
</script>

<template></template>
