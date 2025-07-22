<script lang="ts" setup>
import { useAssignments } from "~/composables/use-assignments";
import type { Department, Official } from "~/types/domain";
import type { Pageable } from "~/types/http";

const department = defineProps<Department>();

const emit = defineEmits<{ close: [] }>();

const pageable = reactive<Pageable>({
  page: 0,
  size: 100,
});

const { getAssignmentsByDepartmentId } = useAssignments();

const { data: page, pending } = await getAssignmentsByDepartmentId(
  department.municipality.id,
  department.id,
  pageable
);

const officialsAlreadyAssigned = computed(() =>
  page.value?.content.map((a) => a.official)
);

const selectedOfficials = ref<Official[]>(officialsAlreadyAssigned.value ?? []);

const { assignToDepartmentInBatch } = useAssignments();
const { loading, handle } = useApiHandler();

const toast = useToast();

async function save() {
  if (selectedOfficials.value.length === 0) return;

  const response = await handle(() =>
    assignToDepartmentInBatch(department.municipality.id, department.id, {
      ids: selectedOfficials.value.map((o) => o.id),
    })
  );

  if (!response) return;

  toast.add({
    title: "Success",
    description: `${response.length} users were assigned to ${department.name}`,
    color: "success",
  });
}
</script>

<template>
  <UModal
    :title="`Assign officials to ${name}`"
    description="Add users to this department."
    :ui="{
      body: 'p-0 sm:p-0',
    }"
  >
    <template #body>
      <div v-if="pending" class="p-4 flex flex-col gap-2">
        <USkeleton class="h-20 w-full" v-for="i in 2" :key="i" />
      </div>
      <MunicipalityOfficialsPalette v-else v-model="selectedOfficials" />
    </template>

    <template #footer>
      <div class="size-full flex gap-2 justify-end">
        <UButton color="neutral" variant="solid" @click="emit('close')"
          >Cancel</UButton
        >
        <UButton
          color="success"
          variant="subtle"
          :loading="loading"
          @click="save"
          >Save</UButton
        >
      </div>
    </template>
  </UModal>
</template>
