<script lang="ts" setup>
const { t } = useI18n();
const route = useRoute();
const id = route.params.id as string;

const { loading, getPointOfInterest, updatePointOfInterest } =
  usePointsOfInterest();
const { data: pointOfInterest, pending } = await getPointOfInterest(id);

useHead({
  title: pointOfInterest.value
    ? t("head.editPointOfInterest.title", { name: pointOfInterest.value.name })
    : t("head.editPointOfInterest.title", { name: "point of interest" }),
  meta: [
    {
      name: "description",
      content: t("head.editPointOfInterest.description"),
    },
  ],
});

definePageMeta({
  name: "Editing point of interest",
  middleware: ["auth"],
});

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
  <EmptyMessage v-if="pending">{{
    t("pages.pointsOfInterest.loading")
  }}</EmptyMessage>
  <EmptyMessage v-else-if="!pointOfInterest">
    {{ t("pages.pointsOfInterest.notFound") }}
  </EmptyMessage>
  <PointsOfInterestForm
    v-else
    :title="
      t('pages.pointsOfInterest.editTitle', { name: pointOfInterest.name })
    "
    :state="pointOfInterest"
    :loading="loading"
    @submit="onSubmit"
  />
</template>
