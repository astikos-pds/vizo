<script lang="ts" setup>
import type { MunicipalityAffiliation } from "~/services/users";

const { t } = useI18n();

const { municipality } = defineProps<MunicipalityAffiliation>();

const formatted = useDateFormat(municipality.createdAt, "DD/MM/YYYY");
</script>

<template>
  <NuxtLink :to="`/municipalities/${municipality.id}`">
    <UCard>
      <div class="flex items-center gap-3">
        <UAvatar
          :src="municipality.iconUrl"
          :alt="municipality.name"
          size="3xl"
        />
        <div class="flex flex-col">
          <h3 class="font-semibold text-lg">{{ municipality.name }}</h3>
          <p>{{ t("municipalities.createdAt", { date: formatted }) }}</p>
        </div>
      </div>
    </UCard>
  </NuxtLink>
</template>
