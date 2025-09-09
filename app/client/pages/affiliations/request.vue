<script lang="ts" setup>
useHead({
  title: "Vizo | Request affiliation",
  meta: [
    {
      name: "description",
      content:
        "Request affiliation to a municipality with your institutional e-mail.",
    },
  ],
});

definePageMeta({
  middleware: ["auth"],
});
</script>

<template>
  <AffiliationsPage>hello 2</AffiliationsPage>
</template>
