<script lang="ts" setup>
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Department, Official } from "~/types/domain";
import type { Pageable } from "~/types/http";

const department = defineProps<Department>();

const emit = defineEmits<{ close: [] }>();

const pageable = reactive<Pageable>({
  page: 0,
  size: 100,
});

const { data: page, pending } =
  municipalityRepository.getAllAssignmentsOfDepartment(
    department.municipality.id,
    department.id,
    pageable,
    {
      key: `municipalities-${department.municipality.id}-departments-${department}-assignments`,
    }
  );

const officialsAlreadyAssigned = computed(() =>
  page.value?.content.map((a) => a.official)
);

const selectedOfficials = ref<Official[]>(officialsAlreadyAssigned.value ?? []);

function save() {}
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
      <div v-if="pending">
        <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
      </div>
      <MunicipalityOfficialsPalette v-else v-model="selectedOfficials" />
    </template>

    <template #footer>
      <div class="size-full flex gap-2 justify-end">
        <UButton color="neutral" variant="solid" @click="emit('close')"
          >Cancel</UButton
        >
        <UButton color="success" variant="subtle" @click="save">Save</UButton>
      </div>
    </template>
  </UModal>
</template>
