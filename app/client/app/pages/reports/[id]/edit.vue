<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import {
  makeUpdateReportSchema,
  type UpdateReportSchema,
} from "~/lib/report-schema";
import type { LatLng } from "~/types/geolocation";

const { t } = useI18n();

useHead({
  title: t("head.editingReport.title"),
  meta: [{ name: "description", content: t("head.editingReport.description") }],
});

definePageMeta({
  name: "Editing report",
  middleware: ["auth"],
});

const route = useRoute();
const reportId = route.params.id as string;

const { getReport, updateReport } = useReports();

const { data: report, pending } = await getReport(reportId);

const images = ref<File[]>([]);

onMounted(async () => {
  if (report.value) {
    const files = await Promise.all(
      report.value.imagesUrls.map((url) => createFileFromUrl(url))
    );

    images.value = files.filter((f) => f !== undefined);
  }
});

async function createFileFromUrl(url: URL) {
  try {
    const response = await fetch(url);
    const blob = await response.blob();
    const file = new File([blob], url.pathname, { type: "'image/jpeg" });
    return file;
  } catch (error) {
    console.error("Error creating file from URL:", error);
    return undefined;
  }
}

const updateReportSchema = makeUpdateReportSchema(t);

const form = reactive<UpdateReportSchema>({
  description: report.value ? report.value.description : "",
  images: [...images.value],
});

const { map, center } = useMap();

const marker = reactive<LatLng>({
  latitude: report.value ? report.value.latitude : center.latitude,
  longitude: report.value ? report.value.longitude : center.longitude,
});

const { isWithinRadius } = useMapGeolocation();

const isMarkerOutOfBounds = computed<boolean>(() => {
  if (!report.value) return false;

  return !isWithinRadius(
    report.value,
    marker,
    REPORT_MAX_RADIUS_TO_UPDATE_COORDINATES_IN_METERS
  );
});

const onSubmit = async (event: FormSubmitEvent<UpdateReportSchema>) => {
  if (isMarkerOutOfBounds.value) return;

  const response = await updateReport(reportId, { ...event.data, ...marker });

  if (!response) return;

  await refreshNuxtData();

  await navigateTo("/reports");
};

const hasUnsavedChanges = computed(() => {
  if (!report.value) return false;

  let hasImagesChanged = false;

  for (let i = 0; i < images.value.length; i++) {
    const image = images.value[i];
    const formImage = form.images[i];

    if (!image || !formImage) continue;

    if (image.name !== formImage.name) {
      hasImagesChanged = true;
    }
  }

  return (
    report.value.description !== form.description ||
    hasImagesChanged ||
    marker.latitude !== report.value.latitude ||
    marker.longitude !== report.value.longitude
  );
});
</script>

<template>
  <EmptyMessage v-if="pending">{{
    t("pages.reports.editing.loading")
  }}</EmptyMessage>
  <EmptyMessage v-if="!report">{{
    t("pages.reports.editing.failedToFetch")
  }}</EmptyMessage>
  <ReportsPage v-else>
    <template #aside>
      <div class="size-full p-3 2xl:p-5 pt-5 flex flex-col items-center">
        <UForm
          :schema="updateReportSchema"
          :state="form"
          @submit="onSubmit"
          class="w-[90%] 2xl:w-[85%] flex flex-col items-center gap-5"
        >
          <UFormField
            :label="t('pages.reportProblem.description')"
            name="description"
            class="w-full"
            required
            ><UTextarea
              v-model="form.description"
              :placeholder="t('pages.reportProblem.descriptionPlaceholder')"
              class="w-full"
              autoresize
          /></UFormField>

          <UFormField
            :label="t('pages.reportProblem.images')"
            name="images"
            class="w-full"
            :hint="t('pages.reportProblem.optional')"
            :description="t('pages.reportProblem.uploadImages')"
          >
            <UFileUpload
              class="w-full"
              v-model="form.images"
              accept="image/*"
              multiple
              label="Drop your image here"
              description="SVG, PNG, JPG or GIF (max. 5MB)"
            />
          </UFormField>

          <UAlert
            v-if="!isMarkerOutOfBounds"
            color="neutral"
            variant="subtle"
            :title="t('pages.reports.editing.alertTitle')"
            :description="t('pages.reports.editing.alertDescription')"
            icon="i-lucide-map-pin"
          />

          <UAlert
            v-else
            color="error"
            variant="subtle"
            :title="t('pages.reports.editing.alertTitle')"
            :description="
              t('pages.reports.editing.alertError', {
                meters: REPORT_MAX_RADIUS_TO_UPDATE_COORDINATES_IN_METERS,
              })
            "
            icon="i-lucide-ban"
          />

          <UButton
            v-if="!hasUnsavedChanges"
            color="neutral"
            variant="outline"
            to="/reports"
          >
            {{ t("pages.reports.editing.cancel") }}
          </UButton>
          <UButton v-else color="neutral" type="submit">
            {{ t("pages.reports.editing.saveChanges") }}
          </UButton>
        </UForm>
      </div>
    </template>

    <Map ref="map" :center="marker" :zoom="18">
      <Marker :key="report.id" v-model="marker" />

      <LCircle
        :lat-lng="[report.latitude, report.longitude]"
        :radius="REPORT_MAX_RADIUS_TO_UPDATE_COORDINATES_IN_METERS"
        color="#0003ff"
        :fill-opacity="0.1"
        :opacity="0.5"
      />

      <CurrentPositionMarker />
    </Map>
  </ReportsPage>
</template>
