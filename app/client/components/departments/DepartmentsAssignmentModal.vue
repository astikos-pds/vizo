<script lang="ts" setup>
import { useAssignedUsers } from "~/composables/use-assigned-users";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Department } from "~/types/domain/department";
import type { Pagination } from "~/types/domain/pagination";

const { t } = useI18n();

const department = defineProps<Department>();

const emit = defineEmits<{ close: [] }>();

const pagination = reactive<Pagination>({
  page: 0,
  size: 100,
});

const { getUsersAssignedToDepartment } = useAssignedUsers();

const { data: page, pending } = await getUsersAssignedToDepartment(
  department.municipality.id,
  department.id,
  pagination
);

const affiliatedUsersAlreadyAssigned = computed(() => {
  if (!page.value) return [];

  return page.value.content.filter((a) => !a.user.isAdmin).map((a) => a.user);
});

const selectedFiliates = ref<AffiliatedUser[]>(
  affiliatedUsersAlreadyAssigned.value ?? []
);

const { loading, assignUsersToDepartment } = useAssignedUsers();

const toast = useToast();

async function save() {
  if (selectedFiliates.value.length === 0) return;

  const response = await assignUsersToDepartment(
    department.municipality.id,
    department.id,
    {
      affiliationsIds: selectedFiliates.value.map((f) => f.id),
    }
  );

  if (!response) return;

  toast.add({
    title: t('components.departments.success'),
    description: `${response.length} ${t('components.departments.usersWereAssigned')} ${department.name}`,
    color: "success",
  });
}
</script>

<template>
  <UModal
    :title="`${t('components.departments.assignTo')} ${name}`"
    :description="t('components.departments.addUsersToThisDepartment')"
    :ui="{
      body: 'p-0 sm:p-0',
    }"
  >
    <template #body>
      <div v-if="pending" class="p-4 flex flex-col gap-2">
        <USkeleton class="h-20 w-full" v-for="i in 2" :key="i" />
      </div>
      <MunicipalitiesFiliatesPalette v-else v-model="selectedFiliates" />
    </template>

    <template #footer>
      <div class="size-full flex gap-2 justify-end">
        <UButton color="neutral" variant="solid" @click="emit('close')">{{
          t('components.departments.cancel')
        }}</UButton
        >
        <UButton
          color="success"
          variant="subtle"
          :loading="loading"
          @click="save"
          >{{ t('components.departments.save') }}</UButton
        >
      </div>
    </template>
  </UModal>
</template>
