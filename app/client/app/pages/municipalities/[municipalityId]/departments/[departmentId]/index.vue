<script lang="ts" setup>
definePageMeta({
  middleware: ["auth", "affiliated"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;
const departmentId = route.params.departmentId as string;

const { getMyAssignmentsInMunicipality } = useMe();

const { data: assignments } = await getMyAssignmentsInMunicipality(
  municipalityId,
  {
    page: 0,
    size: 100,
  }
);

const userStore = useLoggedInUserStore();
userStore.setAssignments(assignments.value ? assignments.value.content : []);

await navigateTo(`/departments/${departmentId}`);
</script>

<template></template>
