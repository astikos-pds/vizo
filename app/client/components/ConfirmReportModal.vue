<script lang="ts" setup>
import type { LatLng } from "~/types/geolocation";

interface Props {
  description: string;
  imagesUrls: string[];
  latLng: LatLng & { accuracy: number };
}
const props = defineProps<Props>();

const emit = defineEmits<{ close: [boolean] }>();
</script>

<template>
  <UModal
    title="Your location is imprecise"
    description="We couldn't track your position precisely, so your report will have less credibility. Are you sure you want to send this report?"
  >
    <template #body>
      <UCard
        :ui="{
          header: 'p-2 sm:px-2.5 2xl:p-2.5 flex flex-row gap-1',
          body: 'p-0 sm:p-0',
          footer: 'p-2 sm:px-2.5 2xl:p-2.5 flex flex-col gap-1',
        }"
      >
        <template #header>
          <span class="font-semibold">Description:</span>
          <p>{{ props.description }}</p>
        </template>

        <section v-if="props.imagesUrls.length !== 0">
          <p class="p-2 font-semibold">Images:</p>
          <section
            class="border-t border-neutral-200 flex flex-row flex-wrap gap-1"
          >
            <div
              class="size-[5rem]"
              v-for="(imageUrl, index) in props.imagesUrls"
            >
              <img
                class="w-full h-full object-cover"
                :src="imageUrl"
                :alt="`Image ${index}`"
                :key="index"
              />
            </div>
          </section>
        </section>

        <template #footer>
          <section class="flex flex-row gap-1">
            <span class="font-semibold">Coordinates:</span>
            <p>{{ latLng.latitude }}, {{ latLng.longitude }}</p>
          </section>

          <section class="flex flex-row gap-1">
            <span class="font-semibold">Distance from actual position:</span>
            <p>{{ latLng.accuracy.toFixed(0) }} meters</p>
          </section>
        </template>
      </UCard>
    </template>
    <template #footer>
      <div class="flex gap-2">
        <UButton
          color="neutral"
          variant="subtle"
          label="Cancel"
          class="cursor-pointer"
          @click="emit('close', false)"
        />
        <UButton
          color="warning"
          label="Send anyway"
          variant="subtle"
          class="cursor-pointer"
          @click="emit('close', true)"
        />
      </div>
    </template>
  </UModal>
</template>
