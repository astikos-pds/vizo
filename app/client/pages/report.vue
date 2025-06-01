<script lang="ts" setup>
import type { RadioGroupItem, RadioGroupValue } from "@nuxt/ui";
import * as z from "zod";
import {
  MAX_FILE_SIZE_IN_MB,
  MAX_FILE_SIZE_IN_BYTES,
  MAX_ACCEPTABLE_ACCURACY_IN_METERS,
} from "~/utils/constants";

const schema = z.object({
  description: z.string().min(1, "Description is required"),
  images: z
    .custom<File[]>()
    .refine(
      (files) => files.length <= 5,
      "You can upload a maximum of 5 images"
    )
    .refine(
      (files) => files.every((file) => file.type.startsWith("image/")),
      "All files must be images"
    )
    .refine(
      (files) => files.every((file) => file.size <= MAX_FILE_SIZE_IN_BYTES),
      `Each image must have at most ${MAX_FILE_SIZE_IN_MB} mb`
    ),
});

type Schema = z.output<typeof schema>;

const form = reactive<Schema>({
  description: "",
  images: [],
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
    label: "Current location",
    description: "Use my current location for reporting.",
    value: "location",
  },
  {
    label: "Point on map",
    description: "Point on map the coordinates of the problem.",
    value: "point",
  },
]);
const locationValue = ref<RadioGroupValue>("location");

const { coords } = useGeolocation();
const CITY_CENTER: [number, number] = [-23.5489, -46.6388];

const mapCenter = computed<[number, number]>(() =>
  !coords.value.accuracy
    ? CITY_CENTER
    : [coords.value.latitude, coords.value.longitude]
);
const markerPosition = ref<[number, number]>(mapCenter.value);

const onSubmit = (e: any) => {
  console.log(form, coords);
};

watch(coords, (newCoords) => {
  if (newCoords.latitude && newCoords.longitude) {
    markerPosition.value = [newCoords.latitude, newCoords.longitude];
  }
});

onBeforeUnmount(() => {
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
});

const formRef = useTemplateRef("formRef");
</script>

<template>
  <section
    class="w-full flex items-center flex-col gap-5 py-20 overflow-y-scroll"
  >
    <h1 class="text-3xl font-semibold text-neutral-900">Report a problem</h1>
    <UForm
      ref="formRef"
      :schema="schema"
      :state="form"
      @submit.prevent="onSubmit"
      class="min-w-[35rem] flex flex-col items-center gap-5"
    >
      <UFormField
        label="Description"
        name="description"
        size="xl"
        class="w-full"
        required
        ><UTextarea
          v-model="form.description"
          placeholder="Buraco na via..."
          size="xl"
          class="w-full"
          autoresize
      /></UFormField>

      <UFormField
        label="Images"
        name="images"
        size="xl"
        class="w-full"
        hint="Optional"
        description="Upload images for more credibility"
      >
        <UButton
          size="xl"
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
            >Escolher arquivos
            <span class="text-neutral-900 font-normal">
              {{
                form.images.length === 0
                  ? "Nenhum arquivo escolhido"
                  : `${form.images.length} arquivos`
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
          size="xl"
        />

        <div v-if="previewUrls.length" class="mt-3 gap-3 flex flex-row">
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
        label="Location"
        size="xl"
        required
        class="w-full"
        :help="
          coords.accuracy > MAX_ACCEPTABLE_ACCURACY_IN_METERS
            ? `${coords.accuracy.toFixed(
                0
              )} meters away. Calculating position more precisely...`
            : ''
        "
      >
        <URadioGroup
          variant="table"
          v-model="locationValue"
          :items="locationItems"
        />
        <Map
          v-if="locationValue === 'point'"
          class="rounded-xl border border-neutral-200 min-w-[30rem] min-h-[25rem] mt-4"
          :zoom="14"
          :center="mapCenter"
        >
          <Marker
            key="1"
            :latitude="markerPosition[0]"
            :longitude="markerPosition[1]"
            :draggable="true"
          />
        </Map>
      </UFormField>
      <UButton type="submit" size="xl" class="justify-center cursor-pointer"
        >Send</UButton
      >
    </UForm>
  </section>
</template>
