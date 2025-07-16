<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { Municipality } from "~/types/domain";

useHead({
  title: "Vizo | New department",
  meta: [
    {
      name: "description",
      content: "Create a new department in this municipality.",
    },
  ],
});

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const departmentSchema = z.object({
  name: z.string().min(1, "Name is required"),
  icon: z
    .custom<File>()
    .optional()
    .refine(
      (file) => !file || file.type.startsWith("image/"),
      "Must be an image"
    )
    .refine(
      (file) => !file || file.size <= MAX_FILE_SIZE_IN_BYTES,
      "Image must have at most 5 mb"
    ),
  colorHex: z.string(),
});

type DepartmentSchema = z.infer<typeof departmentSchema>;

const form = reactive<Partial<DepartmentSchema>>({
  name: "",
  icon: undefined,
  colorHex: "#000000",
});

const previewUrl = ref("");

function handleFileChange(event: Event) {
  const input = event.target as HTMLInputElement;
  if (!input.files) return;

  const file = input.files[0];
  previewUrl.value = URL.createObjectURL(file);
  form.icon = file;
}

onBeforeUnmount(() => {
  if (previewUrl.value) {
    URL.revokeObjectURL(previewUrl.value);
  }
});

const chip = computed(() => ({ backgroundColor: form.colorHex }));

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { data: municipality } = useNuxtData<Municipality>(
  `municipality-${municipalityId}`
);

async function onSubmit(event: FormSubmitEvent<DepartmentSchema>) {
  console.log("sdfsdaf");
  await navigateTo(`/municipalities/${municipalityId}/departments`);
}
</script>

<template>
  <OfficialPage
    title="New department"
    :description="`Create a new department in ${municipality?.name}`"
    class="max-w-180"
  >
    <UForm
      :schema="departmentSchema"
      :state="form"
      @submit="onSubmit"
      class="flex flex-col justify-center items-center gap-5"
    >
      <UFormField label="Name" name="name" required class="w-full">
        <UInput v-model="form.name" class="w-full" />
      </UFormField>

      <UFormField label="Icon" name="icon" hint="Optional" class="w-full">
        <UInput
          type="file"
          accept="image/*"
          class="w-full"
          @change="handleFileChange"
        />

        <div v-if="form.icon" class="w-full flex justify-center p-3">
          <OverlayImage :src="previewUrl" :alt="form.icon.name" />
        </div>
      </UFormField>

      <UFormField label="Color" name="color" hint="Optional" class="w-full">
        <UPopover>
          <UButton
            label="Choose color"
            color="neutral"
            variant="outline"
            class="w-full"
          >
            <template #leading>
              <span :style="chip" class="size-3 rounded-full" />
            </template>
          </UButton>

          <template #content>
            <UColorPicker v-model="form.colorHex" class="p-2" />
          </template>
        </UPopover>
      </UFormField>

      <UButton type="submit">Submit</UButton>
    </UForm>
  </OfficialPage>
</template>
