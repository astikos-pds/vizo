<script lang="ts" setup>
import type { FormSubmitEvent, RadioGroupItem } from "@nuxt/ui";
import {
  MAX_ACCEPTABLE_ACCURACY_IN_METERS,
  MAX_RADIUS_IN_METERS,
} from "~/utils/constants";
import * as z from "zod";
import { useReports } from "~/composables/use-reports";
import type { LatLng } from "~/types/geolocation";
import L from "leaflet";

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
});

type ReportSchema = z.output<typeof reportSchema>;

const form = reactive<ReportSchema>({
  description: "",
  images: [],
  location: "current",
});

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

const { coords, error: geolocationError } = useGeolocation();
const geolocationErrorMessage = computed(() => {
  if (geolocationError.value?.PERMISSION_DENIED)
    return t("reportProblem.localization.error.permissionDenied");
  if (geolocationError.value?.POSITION_UNAVAILABLE)
    return t("reportProblem.localization.error.positionUnavailable");
  if (geolocationError.value?.TIMEOUT)
    return t("reportProblem.localization.error.timeout");
});
const isLocationPrecise = computed(
  () => coords.value.accuracy <= MAX_ACCEPTABLE_ACCURACY_IN_METERS
);
const CITY_CENTER: LatLng = { latitude: -23.5489, longitude: -46.6388 };

const mapCenter = computed<LatLng>(() =>
  !coords.value.accuracy
    ? CITY_CENTER
    : { latitude: coords.value.latitude, longitude: coords.value.longitude }
);
const markerPosition = reactive<LatLng>({
  latitude: mapCenter.value.latitude,
  longitude: mapCenter.value.longitude,
});

function isWithinRadius(
  point1: LatLng,
  point2: LatLng,
  radius: number
): boolean {
  return (
    L.latLng(point1.latitude, point1.longitude).distanceTo(
      L.latLng(point2.latitude, point2.longitude)
    ) <= radius
  );
}

const markerOutOfBounds = computed<boolean>(
  () => !isWithinRadius(coords.value, markerPosition, MAX_RADIUS_IN_METERS)
);
function updateLatLng(newLatLng: LatLng) {
  markerPosition.latitude = newLatLng.latitude;
  markerPosition.longitude = newLatLng.longitude;
}

watch(coords, (newCoords) => {
  if (
    form.location === "current" &&
    newCoords.latitude &&
    newCoords.longitude
  ) {
    markerPosition.latitude = newCoords.latitude;
    markerPosition.longitude = newCoords.longitude;
  }
});

onBeforeUnmount(() => {
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
});

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");
const size = ref<"lg" | "md" | "xl" | "xs" | "sm" | undefined>("xl");

watchEffect(() => {
  size.value = isMobile.value ? "md" : "xl";
});

const formRef = useTemplateRef("formRef");

const { loading, error, report } = useReports();
const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<ReportSchema>) => {
  if (markerOutOfBounds && event.data.location === "point" && isLocationPrecise)
    return;

  const response = await report({
    description: event.data.description,
    images: event.data.images,
    latitude:
      event.data.location === "point"
        ? markerPosition.latitude
        : coords.value.latitude,
    longitude:
      event.data.location === "point"
        ? markerPosition.longitude
        : coords.value.longitude,
  });

  if (!response || error.value) {
    toast.add({
      title: t("toast.error.title"),
      description: t(
        `toast.error.description.${error.value}`,
        t("toast.error.description.500")
      ),
      color: "error",
    });
  } else {
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
</script>

<template>
  <section
    class="w-full h-full flex items-center flex-col gap-5 py-15 sm:py-20 overflow-y-scroll"
  >
    <h1 class="text-3xl text-center font-semibold text-neutral-900 px-2">
      {{ t("reportProblem.title") }}
    </h1>

    <UForm
      ref="formRef"
      :schema="reportSchema"
      :state="form"
      @submit="onSubmit"
      :disabled="geolocationError !== null"
      class="w-[85%] md:max-w-[30rem] lg:min-w-[35rem] mt-3 flex flex-col items-center gap-5"
    >
      <UFormField
        :label="t('reportProblem.description')"
        name="description"
        :size="size"
        class="w-full"
        required
        ><UTextarea
          v-model="form.description"
          :placeholder="t('reportProblem.descriptionPlaceholder')"
          size="xl"
          class="w-full"
          autoresize
      /></UFormField>

      <UFormField
        :label="t('reportProblem.images')"
        name="images"
        :size="size"
        class="w-full"
        :hint="t('reportProblem.optional')"
        :description="t('reportProblem.uploadImages')"
      >
        <UButton
          :size="size"
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
          :size="size"
        />

        <div
          v-if="previewUrls.length"
          class="mt-3 gap-3 flex flex-row flex-wrap"
        >
          <div
            v-for="(url, index) in previewUrls"
            :key="index"
            class="relative rounded-md size-[5rem] border border-neutral-200"
          >
            <UButton
              icon="i-lucide-trash-2"
              variant="subtle"
              size="md"
              class="absolute top-1 right-1 cursor-pointer"
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
        :size="size"
        required
        class="w-full"
        :help="
          coords.accuracy > MAX_ACCEPTABLE_ACCURACY_IN_METERS
            ? t('reportProblem.geolocationAccuracyWarning', {
                accuracy: coords.accuracy.toFixed(0),
              })
            : ''
        "
      >
        <p class="text-error mb-2">{{ geolocationErrorMessage }}</p>
        <URadioGroup
          variant="table"
          v-model="form.location"
          :items="locationItems"
          :size="size"
        />
        <p
          v-if="isLocationPrecise && markerOutOfBounds"
          class="text-error mt-3"
        >
          {{
            t("reportProblem.markerOutOfBounds", {
              radius: MAX_RADIUS_IN_METERS,
            })
          }}
        </p>
        <Map
          v-if="form.location === 'point'"
          class="rounded-xl border border-neutral-200 min-w-[15rem] min-h-[25rem] sm:min-w-[30rem] sm:min-h-[25rem] mt-4"
          :zoom="17"
          :center="[mapCenter.latitude, mapCenter.longitude]"
        >
          <Marker
            key="1"
            :lat-lng="{
              latitude: markerPosition.latitude,
              longitude: markerPosition.longitude,
            }"
            :draggable="true"
            @update:lat-lng="updateLatLng"
          />

          <CurrentPositionMarker
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
      </UFormField>

      <UButton
        type="submit"
        size="xl"
        class="justify-center cursor-pointer"
        :loading="loading"
        :disabled="geolocationError !== null"
        >{{ t("reportProblem.sendButton") }}</UButton
      >
    </UForm>
  </section>
</template>
