<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { PointOfInterest } from "~/types/domain/point-of-interest";

const props = defineProps<{
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
  name: z.string().min(1, "Name is required."),
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
  name: props.state?.name ?? "",
  radius: props.state?.radius ?? 1000,
  colorHex: props.state?.colorHex ?? "#000000",
  active: props.state?.active ?? true,
});

const { map, center } = useMap();

const { coords, isLocationPrecise } = useMapGeolocation();

const marker = reactive({
  latitude: props.state?.latitude ?? center.latitude,
  longitude: props.state?.longitude ?? center.longitude,
});

const stopWatch = watch(
  coords,
  (newCoords) => {
    if (props.state) return;
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
</script>

<template>
  <PointsOfInterestPage :title="title">
    <template #aside>
      <div class="p-3">
        <UButton
          variant="link"
          color="neutral"
          icon="i-lucide-arrow-left"
          to="/points-of-interest"
          >Back</UButton
        >
      </div>
      <main class="flex-1 flex justify-center p-5">
        <UForm
          :schema="pointOfInterestSchema"
          :state="form"
          class="w-[90%] 2xl:w-[85%] flex flex-col items-center gap-6"
          @submit="onSubmit"
        >
          <UFormField label="Name" name="name" required class="w-full">
            <UInput
              v-model="form.name"
              placeholder="Very important thing"
              class="w-full"
            />
          </UFormField>

          <UFormField label="Color" name="color" class="w-full">
            <PopoverColorPicker v-model="form.colorHex" />
          </UFormField>

          <UFormField
            label="Radius"
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
            title="Heads up!"
            description="You may choose the coordinates by moving the marker in the map."
            icon="i-lucide-map-pin"
          />

          <UFormField
            label="Active"
            name="active"
            description="If activated, points of interest notifies you if a new problem appear in it's area."
            class="w-full flex flex-col justify-center items-center gap-2"
          >
            <USwitch v-model="form.active" />
          </UFormField>

          <UButton type="submit" :loading="loading">Save</UButton>
        </UForm>
      </main>
    </template>
    <template #main>
      <div class="size-full flex justify-center items-center">
        <Map ref="map" :zoom="15" :center="props.state ? marker : center">
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
