<script lang="ts" setup>
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
