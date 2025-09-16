<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Department } from "~/types/domain/department";
import type { ProblemType } from "~/types/domain/problem";
import MunicipalitiesFiliatesPalette from "../municipalities/MunicipalitiesFiliatesPalette.vue";

const { state } = defineProps<{
  loading?: boolean;
  state?: Department & { assinees?: AffiliatedUser[] };
  title: string;
  description: string;
}>();

const emit = defineEmits<{
  submit: [data: Omit<DepartmentSchema, "icon"> & { iconUrl?: string }];
}>();

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
  selectedFiliates: z.array(z.custom<AffiliatedUser>()),
  problemTypes: z
    .array(z.custom<ProblemType>())
    .min(1, "At least one problem type is required"),
});

type DepartmentSchema = z.infer<typeof departmentSchema>;

const form = reactive<DepartmentSchema>({
  name: state ? state.name : "",
  icon:
    state && state.iconUrl ? await createFileFromUrl(state.iconUrl) : undefined,
  colorHex: state ? state.colorHex : "#000000",
  selectedFiliates: state && state.assinees ? state.assinees : [],
  problemTypes: state ? state.scope : [],
});

async function createFileFromUrl(url: URL) {
  try {
    const response = await fetch(url);
    const blob = await response.blob();
    const file = new File([blob], url.pathname, { type: "'image/jpeg" });
    return file;
  } catch (error) {
    console.error("Error creating file from URL:", error);
    return undefined;
  }
}

const chip = computed(() => ({ backgroundColor: form.colorHex }));

const { currentAffiliation } = useLoggedInUserStore();

const { loading: imageUploadLoading, uploadImage } = useImageUpload();

async function onSubmit(event: FormSubmitEvent<DepartmentSchema>) {
  const icon = event.data.icon;
  let iconUrl = undefined;

  if (icon) {
    iconUrl = await uploadImage({
      file: icon,
    });
  }

  emit("submit", { ...event.data, iconUrl });
}
</script>

<template>
  <DepartmentsPage
    v-if="currentAffiliation"
    :title="title"
    :description="description"
  >
    <UForm
      :schema="departmentSchema"
      :state="form"
      @submit="onSubmit"
      class="w-full flex flex-col justify-center items-center gap-5"
    >
      <UFormField label="Name" name="name" required class="w-full">
        <UInput v-model="form.name" class="w-full" />
      </UFormField>

      <UFormField
        label="Scope"
        name="scope"
        description="The department's scope refers to the types of problems that it can handle"
        required
        class="w-full"
      >
        <ProblemTypeSelect
          class="w-full"
          multiple
          v-model="form.problemTypes"
        />
      </UFormField>

      <UFormField label="Icon" name="icon" hint="Optional" class="w-full">
        <UFileUpload
          class="w-full"
          v-model="form.icon"
          accept="image/*"
          label="Drop your image here"
          description="SVG, PNG, JPG or GIF (max. 5MB)"
        />
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

      <UFormField
        label="Assign users"
        name="assigned-users"
        hint="Optional"
        class="w-full"
      >
        <MunicipalitiesFiliatesPalette
          class="w-full border border-default rounded-md"
          v-model="form.selectedFiliates"
        />
      </UFormField>

      <UButton type="submit" :loading="loading || imageUploadLoading"
        >Submit</UButton
      >
    </UForm>
  </DepartmentsPage>
</template>
