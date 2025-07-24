<script lang="ts" setup>
import type { Department } from "~/types/domain";

const department = defineProps<Department>();

const formattedCreatedAt = useDateFormat(department.createdAt, "DD/MM/YYYY");

const { isAdmin } = useUserStore();

const { setCurrentDepartment } = useDepartmentStore();
</script>

<template>
  <UButton
    variant="link"
    color="neutral"
    class="p-0"
    to="/dashboard"
    @click="setCurrentDepartment(department)"
  >
    <UCard
      :ui="{
        body: 'p-0 sm:px-0 sm:p-0',
      }"
      class="w-full"
    >
      <div class="p-4 flex items-center justify-between">
        <div class="flex gap-3 items-center">
          <UAvatar :src="iconUrl" :alt="name" size="3xl" />
          <div class="flex flex-col text-start">
            <h3 class="font-semibold 2xl:text-lg text-ellipsis">{{ name }}</h3>
            <span class="text-xs 2xl:text-sm"
              >Created at {{ formattedCreatedAt }}</span
            >
          </div>
        </div>

        <DepartmentCardActions v-if="isAdmin" v-bind="department" />
      </div>
      <div :style="{ backgroundColor: colorHex }" class="w-full h-1"></div>
    </UCard>
  </UButton>
</template>
