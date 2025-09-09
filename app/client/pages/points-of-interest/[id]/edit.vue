<script lang="ts" setup>
const route = useRoute();
const id = route.params.id as string;

const { loading, getPointOfInterest, updatePointOfInterest } =
  usePointsOfInterest();

const { data: pointOfInterest, pending } = await getPointOfInterest(id);

const onSubmit = async (data: {
  name: string;
  radius: number;
  colorHex: string;
  active: boolean;
  latitude: number;
  longitude: number;
}) => {
  const response = await updatePointOfInterest(id, data);

  if (!response) return;

  await navigateTo("/points-of-interest");
};
</script>

<template>
  <EmptyMessage v-if="pending">Loading...</EmptyMessage>
  <EmptyMessage v-else-if="!pointOfInterest">
    Point of interest not found.
  </EmptyMessage>
  <PointsOfInterestForm
    v-else
    :title="`Editing ${pointOfInterest.name}`"
    :state="pointOfInterest"
    :loading="loading"
    @submit="onSubmit"
  />
</template>
