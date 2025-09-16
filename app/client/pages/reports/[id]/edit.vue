<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import {
  updateReportSchema,
  type UpdateReportSchema,
} from "~/lib/report-schema";
import type { LatLng } from "~/types/geolocation";

useHead({
  title: "Editing report",
  meta: [{ name: "description", content: "Edit a report under analysis." }],
});

definePageMeta({
  name: "Editing report",
  middleware: ["auth"],
});

const { t } = useI18n();

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

const form = reactive<UpdateReportSchema>({
  description: report.value ? report.value.description : "",
  images: [...images.value],
});

const { map, center, zoom } = useMap();

const marker = reactive<LatLng>({
  latitude: report.value ? report.value.latitude : center.latitude,
  longitude: report.value ? report.value.longitude : center.longitude,
});

const onSubmit = async (event: FormSubmitEvent<UpdateReportSchema>) => {
  const response = await updateReport(reportId, { ...event.data, ...marker });

  if (!response) return;

  await refreshNuxtData();

  await navigateTo("/reports");
};

const hasUnsavedChanges = computed(() => {
  if (!report.value) return false;

  let hasImagesChanged = false;

  for (let i = 0; i < images.value.length; i++) {
    if (images.value[i].name !== form.images[i].name) {
      hasImagesChanged = true;
    }
  }

  return report.value.description !== form.description || hasImagesChanged;
});
</script>

<template>
  <EmptyMessage v-if="pending">Loading...</EmptyMessage>
  <EmptyMessage v-if="!report">Failed to fetch report.</EmptyMessage>
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
            :label="t('reportProblem.description')"
            name="description"
            class="w-full"
            required
            ><UTextarea
              v-model="form.description"
              :placeholder="t('reportProblem.descriptionPlaceholder')"
              class="w-full"
              autoresize
          /></UFormField>

          <UFormField
            :label="t('reportProblem.images')"
            name="images"
            class="w-full"
            :hint="t('reportProblem.optional')"
            :description="t('reportProblem.uploadImages')"
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

          <UButton
            v-if="!hasUnsavedChanges"
            color="neutral"
            variant="outline"
            to="/reports"
            >Cancel</UButton
          >
          <UButton v-else color="neutral" type="submit">Save changes</UButton>
        </UForm>
      </div>
    </template>

    <Map ref="map" :center="center" :zoom="zoom">
      <Marker :key="report.id" v-model="marker" draggable />

      <CurrentPositionMarker />
    </Map>
  </ReportsPage>
</template>
