<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { PointOfInterest } from "~/types/domain/point-of-interest";

const { t } = useI18n();

const { state } = defineProps<{
  title: string;
  state?: PointOfInterest;
  loading?: boolean;
}>();

const emit = defineEmits<{
  submit: [
    data: PointOfInterestSchema & { latitude: number; longitude: number }
  ];
}>();

const pointOfInterestSchema = z.object({
  name: z.string().min(1, t("components.pointsOfInterest.nameRequired")),
  radius: z
    .number()
    .min(
      POINT_OF_INTEREST_MIN_RADIUS,
      `Radius must be greater than ${POINT_OF_INTEREST_MIN_RADIUS}`
    )
    .max(
      POINT_OF_INTEREST_MAX_RADIUS,
      `Radius must be lower than ${POINT_OF_INTEREST_MAX_RADIUS}`
    ),
  colorHex: z.string().regex(/^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$/, {
    message:
      "Invalid hex color format. Must be a 3- or 6-character hex code (e.g., #RRGGBB or #RGB).",
  }),
  active: z.boolean(),
});

type PointOfInterestSchema = z.infer<typeof pointOfInterestSchema>;

const form = reactive<PointOfInterestSchema>({
  name: state?.name ?? "",
  radius: state?.radius ?? 1000,
  colorHex: state?.colorHex ?? "#000000",
  active: state?.active ?? true,
});

const { map, center } = useMap();

const { coords, isLocationPrecise } = useMapGeolocation();

const marker = reactive({
  latitude:
    state?.latitude ??
    (isLocationPrecise.value ? coords.value.latitude : center.latitude),
  longitude:
    state?.longitude ??
    (isLocationPrecise.value ? coords.value.longitude : center.longitude),
});

const stopWatch = watch(
  coords,
  (newCoords) => {
    if (state) return;
    if (newCoords.latitude === Infinity || newCoords.longitude === Infinity)
      return;

    marker.latitude = newCoords.latitude;
    marker.longitude = newCoords.longitude;

    stopWatch();
  },
  { immediate: true }
);

const onSubmit = (event: FormSubmitEvent<PointOfInterestSchema>) => {
  emit("submit", { ...event.data, ...marker });
};

const hasUnsavedChanges = computed(() => {
  if (!state) return false;

  return (
    state.name !== form.name ||
    state.radius !== form.radius ||
    state.colorHex !== form.colorHex ||
    state.active !== form.active
  );
});
</script>

<template>
  <PointsOfInterestPage :title="title">
    <template #aside>
      <main class="flex-1 flex justify-center lg:p-5 mt-2">
        <UForm
          :schema="pointOfInterestSchema"
          :state="form"
          class="w-[90%] 2xl:w-[85%] flex flex-col items-center gap-6"
          @submit="onSubmit"
        >
          <UFormField
            :label="t('components.pointsOfInterest.name')"
            name="name"
            required
            class="w-full"
          >
            <UInput
              v-model="form.name"
              :placeholder="t('components.pointsOfInterest.veryImportantThing')"
              class="w-full"
            />
          </UFormField>

          <UFormField
            :label="t('components.pointsOfInterest.color')"
            name="color"
            class="w-full"
          >
            <PopoverColorPicker v-model="form.colorHex" />
          </UFormField>

          <UFormField
            :label="t('components.pointsOfInterest.radius')"
            name="radius"
            class="w-full"
            :hint="
              form.radius >= 1000
                ? `${form.radius / 1000} km`
                : `${form.radius} m`
            "
          >
            <USlider
              v-model="form.radius"
              :min="POINT_OF_INTEREST_MIN_RADIUS"
              :max="POINT_OF_INTEREST_MAX_RADIUS"
              class="w-full cursor-pointer"
            />
          </UFormField>

          <UAlert
            color="neutral"
            variant="subtle"
            :title="t('components.pointsOfInterest.headsUp')"
            :description="
              t('components.pointsOfInterest.chooseCoordinatesDescription')
            "
            icon="i-lucide-map-pin"
          />

          <UFormField
            :label="t('components.pointsOfInterest.active')"
            name="active"
            :description="t('components.pointsOfInterest.activeDescription')"
            class="w-full flex justify-between items-center gap-3"
          >
            <USwitch v-model="form.active" />
          </UFormField>

          <UButton
            v-if="state && !hasUnsavedChanges"
            color="neutral"
            variant="outline"
            to="/points-of-interest"
            >{{ t("components.pointsOfInterest.cancel") }}</UButton
          >
          <UButton
            v-else-if="state"
            color="neutral"
            type="submit"
            :loading="loading"
            >{{ t("components.permissionPresets.saveChanges") }}</UButton
          >

          <UButton v-else type="submit" :loading="loading">{{
            t("components.permissionPresets.save")
          }}</UButton>
        </UForm>
      </main>
    </template>
    <template #main>
      <div class="size-full flex justify-center items-center">
        <Map ref="map" :zoom="15" :center="marker">
          <PointsOfInterestMarker
            v-model="marker"
            :radius="form.radius"
            :color-hex="form.colorHex"
            draggable
          />

          <CurrentPositionMarker v-if="isLocationPrecise" v-model="coords" />
        </Map>
      </div>
    </template>
  </PointsOfInterestPage>
</template>
