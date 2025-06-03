<script lang="ts" setup>
import type { FormSubmitEvent, RadioGroupItem } from "@nuxt/ui";
import { MAX_ACCEPTABLE_ACCURACY_IN_METERS } from "~/utils/constants";
import { reportSchema, type ReportSchema } from "~/lib/schema/report-schema";
import { useReports } from "~/composables/use-reports";
import type { LatLng } from "~/types/geolocation";

definePageMeta({
  middleware: ["auth"],
});

const { t } = useI18n();

const form = reactive<ReportSchema>({
  description: "",
  images: [],
  location: "location",
});

const previewUrls = ref<string[]>([]);

const updateImages = (files: File[]) => {
  const combinedFiles = [...form.images, ...files];

  form.images = combinedFiles;

  previewUrls.value = form.images.map((file) => URL.createObjectURL(file));

  nextTick(() => {
    formRef.value?.validate({ name: "images" });
  });
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
    value: "location",
  },
  {
    label: t("reportProblem.localization.point"),
    description: t("reportProblem.localization.pointDescription"),
    value: "point",
  },
]);

const { coords } = useGeolocation();
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

function updateLatLng(newLatLng: LatLng) {
  markerPosition.latitude = newLatLng.latitude;
  markerPosition.longitude = newLatLng.longitude;
}

watch(coords, (newCoords) => {
  if (newCoords.latitude && newCoords.longitude) {
    markerPosition.latitude = newCoords.latitude;
    markerPosition.longitude = newCoords.longitude;
  }
});

onBeforeUnmount(() => {
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
});

const breakpoints = useBreakpoints({
  lg: 1024,
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
      title: "Error",
      description: error.value,
      color: "error",
    });
  } else {
    toast.add({
      title: "Sucess",
      description: "Reported sucessfully with id :" + response.id,
      color: "success",
    });
    form.description = "";
    form.images = [];
    form.location = "location";
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
          :class="
            formRef?.getErrors('images').length ? 'border border-error' : ''
          "
        >
          <label
            for="images"
            class="w-full text-start text-neutral-500 cursor-pointer mx-3 my-2"
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
            ? `${coords.accuracy.toFixed(0)} ${t(
                'reportProblem.geolocationAccuracyWarning'
              )}`
            : ''
        "
      >
        <URadioGroup
          variant="table"
          v-model="form.location"
          :items="locationItems"
          :size="size"
        />
        <Map
          v-if="form.location === 'point'"
          class="rounded-xl border border-neutral-200 min-w-[15rem] min-h-[25rem] sm:min-w-[30rem] sm:min-h-[25rem] mt-4"
          :zoom="14"
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
        </Map>
      </UFormField>

      <UButton
        type="submit"
        size="xl"
        class="justify-center cursor-pointer"
        :loading="loading"
        >{{ t("reportProblem.sendButton") }}</UButton
      >
    </UForm>
  </section>
</template>
