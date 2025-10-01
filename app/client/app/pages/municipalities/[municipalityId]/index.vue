<script lang="ts" setup>
definePageMeta({
  middleware: ["auth"],
});

const { getMyAffiliations } = useMe();

const { data: affiliations } = await getMyAffiliations({
  page: 0,
  size: 100,
});

const userStore = useLoggedInUserStore();
userStore.setAffiliations(affiliations.value ? affiliations.value.content : []);

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

await navigateTo(`/municipalities/${municipalityId}/departments`);
</script>

<template></template>
