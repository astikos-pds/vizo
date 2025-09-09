<script lang="ts" setup>
import type { FormSubmitEvent, RadioGroupItem } from "@nuxt/ui";
import {
  MAX_FILE_SIZE_IN_BYTES,
  MAX_FILE_SIZE_IN_MB,
  MAX_RADIUS_IN_METERS,
  RADIUS_OF_RELATED_REPORTS_IN_METERS,
  REPORT_CONFLICT_PERIOD_IN_MS,
} from "~/utils/constants";
import * as z from "zod";
import { useReports } from "~/composables/use-reports";
import type { LatLng } from "~/types/geolocation";
import { useMapGeolocation } from "~/composables/use-map-geolocation";
import ModalReportImprecision from "~/components/modal/ModalReportImprecision.vue";
import ModalRecentReport from "~/components/modal/ModalRecentReport.vue";
import type { ProblemType } from "~/types/domain/problem";

const { t } = useI18n();

useHead({
  title: t("head.report.title"),
  meta: [{ name: "description", content: t("head.report.description") }],
});

definePageMeta({
  middleware: ["auth"],
});

const reportSchema = z.object({
  description: z
    .string()
    .min(1, t("reportProblem.validation.descriptionRequired")),
  images: z
    .custom<File[]>()
    .refine(
      (files) => files.length <= 5,
      t("reportProblem.validation.maxImages", { count: 5 })
    )
    .refine(
      (files) => files.every((file) => file.type.startsWith("image/")),
      t("reportProblem.validation.mustBeImages")
    )
    .refine(
      (files) => files.every((file) => file.size <= MAX_FILE_SIZE_IN_BYTES),
      t("reportProblem.validation.imageSize", { size: MAX_FILE_SIZE_IN_MB })
    ),
  location: z.string(),
  problemType: z.custom<ProblemType>(),
});

type ReportSchema = z.output<typeof reportSchema>;

const form = reactive<ReportSchema>({
  description: "",
  images: [],
  location: "current",
  problemType: "OTHER",
});

const locationItems = ref<RadioGroupItem[]>([
  {
    label: t("reportProblem.localization.current"),
    description: t("reportProblem.localization.currentDescription"),
    value: "current",
  },
  {
    label: t("reportProblem.localization.point"),
    description: t("reportProblem.localization.pointDescription"),
    value: "point",
  },
]);

const {
  coords,
  error: geolocationError,
  isLocationPrecise,
  markerPosition,
  isWithinRadius,
  updateMarker,
} = useMapGeolocation();

const { center } = useMap();

const geolocationErrorMessage = computed(() => {
  if (geolocationError.value?.PERMISSION_DENIED)
    return t("reportProblem.localization.error.permissionDenied");
  if (geolocationError.value?.POSITION_UNAVAILABLE)
    return t("reportProblem.localization.error.positionUnavailable");
  if (geolocationError.value?.TIMEOUT)
    return t("reportProblem.localization.error.timeout");
});

const isMarkerOutOfBounds = computed<boolean>(
  () => !isWithinRadius(coords.value, markerPosition, MAX_RADIUS_IN_METERS)
);

watch(coords, (newCoords) => {
  updateMarker(newCoords);
});

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");
const size = ref<"lg" | "md" | "xl" | "xs" | "sm" | undefined>("lg");

watchEffect(() => {
  size.value = isMobile.value ? "md" : "xl";
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

  if (response) {
    toast.add({
      title: t("toast.success.title"),
      description: t("toast.success.description.reportCreated", {
        id: response.id,
      }),
      color: "success",
    });

    form.description = "";
    form.images = [];
    form.location = "current";
  }
};

async function shouldSubmitImpreciseLocation(
  data: ReportSchema
): Promise<boolean> {
  if (data.location === "current" && !isLocationPrecise.value) {
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
    data.location === "point" &&
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
  return data.location === "point"
    ? { latitude: markerPosition.latitude, longitude: markerPosition.longitude }
    : coords.value;
}
</script>

<template>
  <section
    class="w-full h-full flex items-center flex-col gap-5 my-15 sm:my-20"
  >
    <h1 class="text-3xl text-center font-semibold px-2">
      {{ t("reportProblem.title") }}
    </h1>

    <UForm
      :schema="reportSchema"
      :state="form"
      @submit="onSubmit"
      :disabled="geolocationError !== null"
      class="w-80 md:w-120 lg:w-130 xl:w-150 mt-3 flex flex-col items-center gap-5"
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
        class="w-full"
        :help="
          !isLocationPrecise && !geolocationError && coords.accuracy > 0
            ? t('reportProblem.geolocationAccuracyWarning', {
                accuracy: coords.accuracy.toFixed(0),
              })
            : ''
        "
      >
        <URadioGroup
          variant="table"
          v-model="form.location"
          :items="locationItems"
        />
        <p class="text-error mt-3">{{ geolocationErrorMessage }}</p>

        <Map
          v-if="form.location === 'point'"
          class="rounded-xl border border-default min-w-[15rem] min-h-[25rem] sm:min-w-[30rem] sm:min-h-[25rem] mt-4"
          :zoom="16"
          :center="center"
        >
          <Marker
            key="1"
            :lat-lng="{
              latitude: markerPosition.latitude,
              longitude: markerPosition.longitude,
            }"
            :draggable="true"
            @update:lat-lng="updateMarker"
          />

          <CurrentPositionMarker
            v-if="isLocationPrecise && !geolocationError"
            v-model="coords"
          />

          <LCircle
            v-if="isLocationPrecise"
            :lat-lng="[coords.latitude, coords.longitude]"
            :radius="MAX_RADIUS_IN_METERS"
            fill-color="#0003ff"
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

      <UFormField
        label="Problem type"
        name="problemType"
        required
        class="w-full"
      >
        <ProblemTypeSelect class="w-full" v-model="form.problemType" />
      </UFormField>

      <UButton
        type="submit"
        :loading="loading"
        :disabled="geolocationError !== null"
        >{{ t("reportProblem.sendButton") }}</UButton
      >
    </UForm>
  </section>
</template>
