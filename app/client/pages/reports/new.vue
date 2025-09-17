<script lang="ts" setup>
import type { FormSubmitEvent, RadioGroupItem } from "@nuxt/ui";
import {
  MAX_RADIUS_IN_METERS,
  RADIUS_OF_RELATED_REPORTS_IN_METERS,
  REPORT_CONFLICT_PERIOD_IN_MS,
} from "~/utils/constants";
import { useReports } from "~/composables/use-reports";
import { useMapGeolocation } from "~/composables/use-map-geolocation";
import ModalReportImprecision from "~/components/modal/ModalReportImprecision.vue";
import ModalRecentReport from "~/components/modal/ModalRecentReport.vue";
import { reportSchema, type ReportSchema } from "~/lib/report-schema";

const { t } = useI18n();

useHead({
  title: t("head.report.title"),
  meta: [{ name: "description", content: t("head.report.description") }],
});

definePageMeta({
  name: "Report a problem",
  middleware: ["auth"],
});

const {
  coords,
  error: geolocationError,
  isLocationPrecise,

  isWithinRadius,
} = useMapGeolocation();

const form = reactive<ReportSchema>({
  description: "",
  images: [],
  location: "CURRENT",
});

watch(geolocationError, (newError) => {
  if (!newError) return;

  form.location = "POINT";
});

const locationItems = ref<RadioGroupItem[]>([
  {
    label: t("reportProblem.localization.current"),
    description: t("reportProblem.localization.currentDescription"),
    value: "CURRENT",
  },
  {
    label: t("reportProblem.localization.point"),
    description: t("reportProblem.localization.pointDescription"),
    value: "POINT",
  },
]);

const { center } = useMap();

const isMarkerOutOfBounds = computed<boolean>(
  () =>
    isLocationPrecise &&
    !isWithinRadius(coords.value, marker.value, MAX_RADIUS_IN_METERS)
);

const marker = ref(center);

const locationMessage = computed(() => {
  if (
    geolocationError.value?.code === 2 ||
    geolocationError.value?.code === 3
  ) {
    return "Position unavailable or timeout for precise coordinates.";
  }

  if (geolocationError.value?.code === 1) {
    return "Permission to track current geolocation denied, please point the problem on the map or enable location tracking.";
  }

  if (!isLocationPrecise.value) {
    return t("reportProblem.geolocationAccuracyWarning", {
      accuracy: coords.value.accuracy.toFixed(0),
    });
  }
});

const { loading, report } = useReports();
const { getMyReports } = useMe();
const toast = useToast();

const overlay = useOverlay();

const onSubmit = async (event: FormSubmitEvent<ReportSchema>) => {
  if (!(await shouldSubmitImpreciseLocation(event.data))) return;
  if (!(await shouldSubmitOutOfBounds(event.data))) return;

  const { latitude, longitude } = resolveCoordinates(event.data);

  if (
    !(await shouldSubmitIfRecentlyReported({
      latitude,
      longitude,
      description: event.data.description,
      imagesUrls: event.data.images.map((f) => URL.createObjectURL(f)),
    }))
  )
    return;

  const response = await report({
    ...event.data,
    latitude: latitude,
    longitude: longitude,
  });

  if (!response) return;

  toast.add({
    title: t("toast.success.title"),
    description: "Report created successfully",
    color: "success",
  });

  await navigateTo("/");
};

async function shouldSubmitImpreciseLocation(
  data: ReportSchema
): Promise<boolean> {
  if (data.location === "CURRENT" && !isLocationPrecise.value) {
    const modal = overlay.create(ModalReportImprecision, {
      props: {
        description: data.description,
        imagesUrls: data.images.map((f) => URL.createObjectURL(f)),
        latLng: coords.value,
      },
    });
    return await modal.open().result;
  }
  return true;
}

async function shouldSubmitOutOfBounds(data: ReportSchema): Promise<boolean> {
  if (
    isMarkerOutOfBounds.value &&
    data.location === "POINT" &&
    isLocationPrecise.value
  ) {
    return false;
  }
  return true;
}

async function shouldSubmitIfRecentlyReported(report: {
  latitude: number;
  longitude: number;
  description: string;
  imagesUrls: string[];
}): Promise<boolean> {
  const { data: nearbyReports } = await getMyReports({
    latitude: report.latitude,
    longitude: report.longitude,
    radius: RADIUS_OF_RELATED_REPORTS_IN_METERS,
    page: 0,
    size: 1,
  });

  if (!nearbyReports.value || nearbyReports.value.content.length === 0)
    return true;

  const lastReport = nearbyReports.value.content[0];
  const diff = Math.abs(
    new Date().getTime() - new Date(lastReport.createdAt).getTime()
  );

  if (diff <= REPORT_CONFLICT_PERIOD_IN_MS) {
    const modal = overlay.create(ModalRecentReport, {
      props: {
        lastReport: {
          ...lastReport,
          imagesUrls: lastReport.imagesUrls.map((url) => url.toString()),
        },
        currentReport: report,
      },
    });
    return await modal.open().result;
  }

  return true;
}

function resolveCoordinates(data: ReportSchema) {
  return data.location === "POINT"
    ? { latitude: marker.value.latitude, longitude: marker.value.longitude }
    : coords.value;
}
</script>

<template>
  <ReportsPage>
    <div
      class="w-[90%] md:w-[75%] xl:w-[65%] 2xl:w-[55%] h-full flex flex-col gap-5 py-10 p-5 xl:p-10"
    >
      <h1 class="text-3xl text-center font-semibold px-2">
        {{ t("reportProblem.title") }}
      </h1>

      <UForm
        :schema="reportSchema"
        :state="form"
        @submit="onSubmit"
        class="mt-3 flex flex-col items-center gap-5 pb-15"
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

        <UFormField
          :label="t('reportProblem.locationLabel')"
          required
          :help="locationMessage"
          class="w-full"
        >
          <URadioGroup
            variant="table"
            v-model="form.location"
            :items="locationItems"
            :disabled="geolocationError !== null"
          />

          <Map
            v-if="form.location === 'POINT'"
            class="rounded-xl border border-default mt-4 min-w-full min-h-120"
            :zoom="16"
            :center="center"
          >
            <Marker v-model:lat-lng="marker" :draggable="true" />

            <CurrentPositionMarker
              v-if="isLocationPrecise && !geolocationError"
              v-model="coords"
            />

            <LCircle
              v-if="isLocationPrecise"
              :lat-lng="[coords.latitude, coords.longitude]"
              :radius="MAX_RADIUS_IN_METERS"
              color="#0003ff"
              :fill-opacity="0.1"
              :opacity="0.5"
            />
          </Map>

          <p
            v-if="isLocationPrecise && isMarkerOutOfBounds"
            class="text-error mt-3"
          >
            {{
              t("reportProblem.markerOutOfBounds", {
                radius: MAX_RADIUS_IN_METERS,
              })
            }}
          </p>
        </UFormField>

        <UButton type="submit" :loading="loading">{{
          t("reportProblem.sendButton")
        }}</UButton>
      </UForm>
    </div>
  </ReportsPage>
</template>
