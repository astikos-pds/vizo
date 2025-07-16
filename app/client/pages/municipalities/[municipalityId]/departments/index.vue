<script lang="ts" setup>
import { municipalityRepository } from "~/repositories/municipality-repository";
import { userRepository } from "~/repositories/user-repository";
import type { Municipality } from "~/types/domain";
import type { Pageable } from "~/types/http";

useHead({
  title: "Vizo | Departments",
  meta: [
    {
      name: "description",
      content: "Find all departments of this municipality.",
    },
  ],
});

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { data: municipality } = useNuxtData<Municipality>(
  `municipality-${municipalityId}`
);

const { data: assignments, pending } =
  userRepository.getAllUserAssignmentsByMunicipalityId(municipalityId, {
    key: `municipalities-${municipalityId}-assignments`,
  });

const departments = computed(() => assignments.value?.map((a) => a.department));

const newDepartmentLink = `/municipalities/${municipalityId}/departments/new`;
</script>

<template>
  <OfficialPage
    title="Departments"
    :description="`Enter in one of the departments of ${municipality?.name}.`"
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <div v-else class="size-full flex flex-col">
      <div class="flex my-4 justify-between items-center">
        <span class="text-muted">
          Encontered {{ (departments ?? []).length }} department(s)
        </span>

        <UButton icon="i-lucide-plus" :to="newDepartmentLink"
          >New department</UButton
        >
      </div>

      <NotFoundMessage v-if="departments?.length === 0"
        >No departments found.</NotFoundMessage
      >
      <div
        v-else
        class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4"
      >
        <DepartmentCard
          v-for="department in departments"
          v-bind="department"
          :key="department.id"
        />
      </div>
    </div>
  </OfficialPage>
</template>
