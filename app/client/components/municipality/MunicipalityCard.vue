<script lang="ts" setup>
import type { AffiliationRequest } from "~/types/domain";

const { t } = useI18n();

const affiliation = defineProps<AffiliationRequest>();

const formattedCreatedAt = useDateFormat(
  affiliation.municipality.createdAt,
  "DD/MM/YYYY"
);

const formattedApprovedAt = computed(() => {
  if (affiliation.approvedAt) {
    return useDateFormat(affiliation.approvedAt, "DD/MM/YYYY");
  }
});

function exitMunicipality(id: string) {}
</script>

<template>
  <NuxtLink :to="`/municipalities/${municipality.id}`">
    <UCard
      :ui="{
        footer: 'p-2 sm:p-2',
      }"
    >
      <div class="flex items-center gap-3">
        <UAvatar
          :src="municipality.iconUrl"
          :alt="municipality.name"
          size="3xl"
        />
        <div class="flex flex-col">
          <h3 class="font-semibold text-lg">{{ municipality.name }}</h3>
          <div class="text-sm flex flex-col">
            <span>
              {{ t("municipalities.createdAt", { date: formattedCreatedAt }) }}
            </span>
          </div>
        </div>
      </div>

      <template #footer v-if="official.role !== 'ADMIN'">
        <div class="w-full flex justify-between items-center">
          <div class="text-xs flex flex-col">
            <span v-if="approvedAt">Since {{ formattedApprovedAt }}</span>
            <span v-if="approvedBy">Approved by {{ approvedBy.name }}</span>
          </div>

          <div>
            <UButton color="error" size="xs" @click="exitMunicipality(id)"
              >Exit</UButton
            >
          </div>
        </div>
      </template>
    </UCard>
  </NuxtLink>
</template>
