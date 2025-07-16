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
import type { ProblemType, Report, ReportImage } from "~/types/domain";

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
  problemType: "",
});

const formRef = useTemplateRef("formRef");
const previewUrls = ref<string[]>([]);

const updateImages = (files: File[]) => {
  const combinedFiles = [...form.images, ...files];

  form.images = combinedFiles;

  previewUrls.value = form.images.map((file) => URL.createObjectURL(file));

  formRef.value?.validate({ name: "images" });
};

const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (!input.files) return;
  updateImages(Array.from(input.files));
  input.value = "";
};

const handleFileRemove = (index: number) => {
  if (previewUrls.value[index]) {
    URL.revokeObjectURL(previewUrls.value[index]);
  }
  previewUrls.value.splice(index, 1);
  form.images.splice(index, 1);
  formRef.value?.validate({ name: "images" });
};

onBeforeUnmount(() => {
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
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
  mapCenter,
  markerPosition,
  isWithinRadius,
  updateMarker,
} = useMapGeolocation();

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
  if (!isLocationPrecise.value) {
    updateMarker(newCoords);
  }
});

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");
const size = ref<"lg" | "md" | "xl" | "xs" | "sm" | undefined>("lg");

watchEffect(() => {
  size.value = isMobile.value ? "md" : "xl";
});

const { loading, report, getReports } = useReports();
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
      images: previewUrls.value.map<ReportImage>((p) => {
        return { url: p };
      }),
    } as Report))
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
        imagesUrls: previewUrls.value,
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

async function shouldSubmitIfRecentlyReported(
  report: Report
): Promise<boolean> {
  const nearbyReports = await getReports({
    filter: {
      latitude: report.latitude,
      longitude: report.longitude,
      radius: RADIUS_OF_RELATED_REPORTS_IN_METERS,
    },
    pageable: { size: 1 },
  });

  if (!nearbyReports || nearbyReports.content.length === 0) return true;

  const lastReport = nearbyReports.content[0];
  const diff = Math.abs(
    new Date().getTime() - new Date(lastReport.createdAt).getTime()
  );

  if (diff <= REPORT_CONFLICT_PERIOD_IN_MS) {
    const modal = overlay.create(ModalRecentReport, {
      props: { lastReport: lastReport, currentReport: report },
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
      ref="formRef"
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
        <UButton
          color="neutral"
          variant="outline"
          class="w-full p-0"
          :disabled="geolocationError !== null"
          :class="
            formRef?.getErrors('images').length ? 'border border-error' : ''
          "
        >
          <label
            for="images"
            class="w-full text-start text-neutral-500 cursor-pointer mx-3 my-2"
            :class="
              geolocationError !== null && 'disabled hover:cursor-not-allowed'
            "
            >{{ t("reportProblem.chooseFile") }}
            <span class="text-neutral-900 font-normal">
              {{
                form.images.length === 0
                  ? t("reportProblem.selectedFile")
                  : `${form.images.length} ${t("reportProblem.files")}`
              }}</span
            ></label
          >
        </UButton>
        <UInput
          :key="form.images.length"
          id="images"
          type="file"
          multiple
          accept="image/*"
          @change="handleFileChange"
          class="sr-only"
        />

        <div
          v-if="previewUrls.length"
          class="mt-3 gap-3 flex flex-row flex-wrap"
        >
          <div
            v-for="(url, index) in previewUrls"
            :key="index"
            class="relative rounded-md size-[5rem] border border-default"
          >
            <UButton
              icon="i-lucide-trash-2"
              variant="solid"
              size="md"
              color="info"
              class="absolute top-1 right-1"
              @click="handleFileRemove(index)"
            />
            <img
              :src="url"
              :alt="`Preview ${index}`"
              class="w-full h-full object-cover rounded-md"
            />
          </div>
        </div>
      </UFormField>

      <UFormField
        :label="t('reportProblem.locationLabel')"
        required
        class="w-full"
        :help="
          !isLocationPrecise && !geolocationError
            ? t('reportProblem.geolocationAccuracyWarning', {
                accuracy:
                  coords.accuracy > 0
                    ? coords.accuracy.toFixed(0)
                    : 'inacessible',
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
          :center="[mapCenter.latitude, mapCenter.longitude]"
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
            :lat-lng="{
              latitude: coords.latitude,
              longitude: coords.longitude,
            }"
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
