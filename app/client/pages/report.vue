<script lang="ts" setup>
import * as z from "zod";

const MAX_FILE_SIZE_IN_MB = 5 * 1024 * 1024;

const schema = z.object({
  description: z.string().min(8, "Must be at least 8 characters"),
  image: z.string(),
});

type Schema = z.output<typeof schema>;

const form = reactive<Schema>({
  description: "",
  image: "",
});

const { coords, locatedAt, error } = useGeolocation();

console.log(form);
</script>

<template>
  <section
    class="min-h-screen w-full flex justify-center items-center flex-col gap-5"
  >
    <h1 class="text-3xl">Report a problem</h1>
    <UForm
      :schema="schema"
      :state="form"
      class="min-w-[35%] flex flex-col items-center gap-5"
    >
      <UFormField
        label="Description"
        name="description"
        size="xl"
        hint="Optional"
        class="w-full"
        ><UTextarea
          v-model="form.description"
          placeholder="Buraco na via..."
          class="w-full"
      /></UFormField>

      <UFormField label="Image" name="image" size="xl" class="w-full">
        <UInput type="file" v-model="form.image" size="xl" class="w-full" />
      </UFormField>

      <UFormField label="Location" size="xl">
        <Map
          class="rounded-2xl min-w-[35rem] min-h-[30rem]"
          :zoom="18"
          :center="[-23.6045, -46.6694]"
        >
          <Marker key="1" :latitude="-23.6045" :longitude="-46.6694" />
        </Map>
      </UFormField>

      <UButton type="submit" size="xl" class="justify-center">Send</UButton>
    </UForm>
  </section>
</template>
