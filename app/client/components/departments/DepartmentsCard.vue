<script lang="ts" setup>
import type { Department } from "~/types/domain/department";
import DepartmentsCardActions from "./DepartmentsCardActions.vue";

const { t, locale } = useI18n();

const department = defineProps<Department>();

const { currentAffiliation } = useLoggedInUserStore();
</script>

<template>
  <NuxtLink
    v-if="currentAffiliation"
    :to="`/municipalities/${department.municipality.id}/departments/${department.id}`"
  >
    <UCard
      :ui="{
        body: 'p-0 sm:px-0 sm:p-0',
      }"
      class="w-full"
    >
      <div class="p-4 flex items-center justify-between">
        <div class="flex gap-3 items-center">
          <UAvatar
            :src="department.iconUrl?.toString()"
            :alt="department.name"
            size="3xl"
          />
          <div class="flex flex-col text-start gap-2">
            <div class="flex flex-col">
              <h3 class="font-semibold 2xl:text-lg text-ellipsis">
                {{ department.name }}
              </h3>
              <span class="text-xs 2xl:text-sm"
                >{{ t('components.departments.createdAt') }}
                {{ department.createdAt.toLocaleDateString(locale) }}</span
              >
            </div>
            <div class="w-full flex gap-1 flex-wrap">
              <UBadge
                v-for="problemType in department.scope"
                :key="problemType"
                color="neutral"
                variant="outline"
                size="sm"
                >{{ problemType }}</UBadge
              >
            </div>
          </div>
        </div>

        <DepartmentsCardActions
          v-if="currentAffiliation.isAdmin"
          v-bind="department"
        />
      </div>
      <div
        :style="{ backgroundColor: department.colorHex }"
        class="w-full h-1"
      ></div>
    </UCard>
  </NuxtLink>
</template>
