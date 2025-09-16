<script lang="ts" setup>
useHead({
  title: "Vizo | New point of interest",
  meta: [
    {
      name: "description",
      content: "Create a new point of interest to keep up with the city.",
    },
  ],
});

definePageMeta({
  name: "New point of interest",
  middleware: ["auth"],
});

const { createPointOfInterest, loading } = usePointsOfInterest();

const onSubmit = async (data: {
  name: string;
  radius: number;
  colorHex: string;
  active: boolean;
  latitude: number;
  longitude: number;
}) => {
  const response = await createPointOfInterest(data);

  if (!response) return;

  await navigateTo("/points-of-interest");
};
</script>

<template>
  <PointsOfInterestForm
    title="New point of interest"
    @submit="onSubmit"
    :loading="loading"
  />
</template>
