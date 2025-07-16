<script lang="ts" setup>
import type { Department, Official } from "~/types/domain";

const department = defineProps<Department>();

const departmentLink = `/municipalities/${department.municipality.id}/departments/${department.id}`;

const formattedCreatedAt = useDateFormat(department.createdAt, "DD/MM/YYYY");

const { isAdmin } = useUserStore();
</script>

<template>
  <NuxtLink :to="departmentLink">
    <UCard
      :ui="{
        body: 'p-0 sm:px-0 sm:p-0',
      }"
    >
      <div class="p-4 flex items-center justify-between">
        <div class="flex gap-3 items-center">
          <UAvatar :src="iconUrl" :alt="name" size="3xl" />
          <div class="flex flex-col">
            <h3 class="font-semibold text-lg">{{ name }}</h3>
            <span class="text-sm">Created at {{ formattedCreatedAt }}</span>
          </div>
        </div>

        <DepartmentCardActions v-if="isAdmin" v-bind="department" />
      </div>
      <div :style="{ backgroundColor: colorHex }" class="w-full h-1"></div>
    </UCard>
  </NuxtLink>
</template>
