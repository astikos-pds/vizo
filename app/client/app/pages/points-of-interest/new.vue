<script lang="ts" setup>
const { t } = useI18n();

useHead({
  title: t("head.newPointOfInterest.title"),
  meta: [
    {
      name: "description",
      content: t("head.newPointOfInterest.description"),
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
    :title="t('pages.pointsOfInterest.newTitle')"
    @submit="onSubmit"
    :loading="loading"
  />
</template>
