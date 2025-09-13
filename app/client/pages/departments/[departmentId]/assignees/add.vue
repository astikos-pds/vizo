<script lang="ts" setup>
useHead({
  title: "Vizo | Assign users to department",
  meta: [
    {
      name: "description",
      content: "Assign user to a department",
    },
  ],
});

definePageMeta({
  name: "Assign",
  middleware: ["auth", "assigned"],
});
</script>

<template>
  <AssignedUsersPage>hello 2</AssignedUsersPage>
</template>
