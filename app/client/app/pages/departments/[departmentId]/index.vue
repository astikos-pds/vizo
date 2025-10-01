<script lang="ts" setup>
definePageMeta({
  middleware: ["auth", "assigned"],
});

const { currentAssignment } = useLoggedInUserStore();

await navigateTo(
  currentAssignment
    ? `/departments/${currentAssignment.department.id}/dashboard`
    : "/affiliations"
);
</script>

<template></template>
