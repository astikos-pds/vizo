<script lang="ts" setup>
import type { LatLng } from "~/types/geolocation";

interface Props {
  description: string;
  imagesUrls: string[];
  latLng: LatLng & { accuracy: number };
}
const props = defineProps<Props>();

const emit = defineEmits<{ close: [boolean] }>();

const { t } = useI18n();
</script>

<template>
  <UModal
    :title="t('imprecisionModal.title')"
    :description="t('imprecisionModal.description')"
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
          <span class="font-semibold">{{
            t("imprecisionModal.descriptionLabel")
          }}</span>
          <p>{{ props.description }}</p>
        </template>

        <section v-if="props.imagesUrls.length !== 0">
          <p class="p-2 font-semibold">
            {{ t("imprecisionModal.imagesLabel") }}
          </p>
          <section
            class="border-t border-default flex flex-row flex-wrap gap-1"
          >
            <div
              class="size-[5rem]"
              v-for="(imageUrl, index) in props.imagesUrls"
              :key="index"
            >
              <img
                class="w-full h-full object-cover"
                :src="imageUrl"
                :alt="`${t('imprecisionModal.imageAlt')} ${index + 1}`"
              />
            </div>
          </section>
        </section>

        <template #footer>
          <section class="flex flex-row gap-1">
            <span class="font-semibold">{{
              t("imprecisionModal.coordinatesLabel")
            }}</span>
            <p>{{ latLng.latitude }}, {{ latLng.longitude }}</p>
          </section>

          <section class="flex flex-row gap-1">
            <span class="font-semibold">{{
              t("imprecisionModal.accuracyLabel")
            }}</span>
            <p>
              {{ latLng.accuracy.toFixed(0) }}
              {{ t("imprecisionModal.meters") }}
            </p>
          </section>
        </template>
      </UCard>
    </template>
    <template #footer>
      <div class="flex gap-2">
        <UButton
          color="neutral"
          variant="subtle"
          :label="t('common.cancel')"
          @click="emit('close', false)"
        />
        <UButton
          color="warning"
          variant="subtle"
          :label="t('imprecisionModal.confirm')"
          @click="emit('close', true)"
        />
      </div>
    </template>
  </UModal>
</template>
