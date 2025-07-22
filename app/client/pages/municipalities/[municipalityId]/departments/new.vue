<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useDepartments } from "~/composables/use-departments";
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Municipality, Official, ProblemType } from "~/types/domain";

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
  selectedOfficials: z.array(z.custom<Official>()),
  problemTypes: z.array(z.custom<ProblemType>()),
});

type DepartmentSchema = z.infer<typeof departmentSchema>;

const form = reactive<DepartmentSchema>({
  name: "",
  icon: undefined,
  colorHex: "#000000",
  selectedOfficials: [],
  problemTypes: [],
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

const { uploadImage } = useImage();
const { loading, handle } = useApiHandler();

const toast = useToast();

async function onSubmit(event: FormSubmitEvent<DepartmentSchema>) {
  const icon = event.data.icon;

  let iconUrl = "";
  if (icon) {
    iconUrl =
      (await handle(() =>
        uploadImage({
          file: icon,
        })
      )) ?? "";
  }

  const department = await handle(() =>
    useDepartments().createDepartment(municipalityId, {
      name: event.data.name,
      iconUrl,
      colorHex: event.data.colorHex,
      problemTypes: event.data.problemTypes,
    })
  );

  if (!department) return;

  const assignments = await handle(() =>
    municipalityRepository.assignToDepartmentInBatch(
      municipalityId,
      department.id,
      {
        ids: event.data.selectedOfficials.map((o) => o.id),
      }
    )
  );

  if (!assignments) return;

  toast.add({
    title: "Success",
    description: "Department created successfully!",
    color: "success",
  });

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

      <MunicipalityOfficialsPalette
        class="w-full border border-default rounded-md"
        v-model="form.selectedOfficials"
      />

      <UFormField
        label="Visible problem types"
        name="problemTypes"
        required
        class="w-full"
      >
        <ProblemTypeSelect
          class="w-full"
          multiple
          v-model="form.problemTypes"
        />
      </UFormField>

      <UButton type="submit" :loading="loading">Submit</UButton>
    </UForm>
  </OfficialPage>
</template>
