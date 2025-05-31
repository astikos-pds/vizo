<script lang="ts" setup>
import type { RadioGroupItem, RadioGroupValue } from "@nuxt/ui";
import * as z from "zod";

const MAX_FILE_SIZE_IN_MB = 5;
const MAX_FILE_SIZE_IN_BYTES = MAX_FILE_SIZE_IN_MB * 1024 * 1024;

const schema = z.object({
  description: z.string().min(1, "Description is required"),
  images: z
    .array(
      z
        .instanceof(File)
        .refine(
          (file) => file.type.startsWith("image/"),
          "All files must be images"
        )
        .refine(
          (file) => file.size <= MAX_FILE_SIZE_IN_BYTES,
          `Each image must have at most ${MAX_FILE_SIZE_IN_MB} mb`
        )
    )
    .max(5, "You can upload a maximum of 5 images"),
});

type Schema = z.output<typeof schema>;

const form = reactive<Schema>({
  description: "",
  images: [],
});

const previewUrls = ref<string[]>([]);

const updateImages = (files: File[]) => {
  form.images = files;

  if (files.some((file) => !file.type.startsWith("image/"))) return;

  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
  previewUrls.value = files.map((file) => URL.createObjectURL(file));
};

const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (!input.files) return;
  updateImages(Array.from(input.files));
  input.value = "";
};

const handleFileRemove = (index: number) => {
  const newFiles = [...form.images];
  newFiles.splice(index, 1);
  updateImages(newFiles);
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

const { coords } = useGeolocation({
  enableHighAccuracy: true,
  timeout: 10000,
  maximumAge: 0,
});
const center = ref<[number, number]>([-23.5489, -46.6388]);
const mapCenter = ref<[number, number]>([
  coords.value.accuracy > 100 ? center.value[0] : coords.value.latitude,
  coords.value.accuracy > 100 ? center.value[1] : coords.value.longitude,
]);
const markerPosition = ref<[number, number]>([...mapCenter.value]);

const onSubmit = (e: any) => {
  console.log(form, coords);
};

onBeforeUnmount(() => {
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));
});
</script>

<template>
  <section
    class="w-full flex items-center flex-col gap-5 py-20 overflow-y-scroll"
  >
    <h1 class="text-3xl font-semibold text-neutral-900">Report a problem</h1>
    <UForm
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
        <UButton size="xl" color="neutral" variant="outline" class="w-full p-0">
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
          id="images"
          type="file"
          multiple
          accept="image/*"
          @change="handleFileChange"
          size="xl"
          class="hidden"
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
              class="absolute top-1 left-1 cursor-pointer"
              @click="handleFileRemove(index)"
            />
            <img
              :src="url"
              :alt="`Preview ${index}`"
              class="w-full h-full object-cover rounded-md"
            />
          </div>
        </div>

        <div v-if="form.images.length > 5" class="text-error mt-2">
          You can upload a maximum of 5 images
        </div>
        <div
          v-if="form.images.some((image) => !image.type.startsWith('image/'))"
          class="text-error mt-2"
        >
          All files must be images
        </div>
        <div
          v-if="
            form.images.some((image) => image.size > MAX_FILE_SIZE_IN_BYTES)
          "
          class="text-error mt-2"
        >
          Each image must have at most {{ MAX_FILE_SIZE_IN_MB }} mb
        </div>
      </UFormField>

      <UFormField label="Location" size="xl" required class="w-full">
        <URadioGroup
          variant="table"
          :disabled="coords.accuracy > 100"
          v-model="locationValue"
          :items="locationItems"
        />
        <Map
          v-if="locationValue === 'point'"
          class="rounded-xl border border-neutral-200 min-w-[30rem] min-h-[25rem] mt-4"
          :zoom="18"
          :center="[
            coords.accuracy > 100 ? center[0] : coords.latitude,
            coords.accuracy > 100 ? center[1] : coords.longitude,
          ]"
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
