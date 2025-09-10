<script lang="ts" setup>
import DepartmentsCard from "~/components/departments/DepartmentsCard.vue";
import type { Pagination } from "~/types/domain/pagination";

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
  middleware: ["auth", "affiliated"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const pagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { getMyAssignmentsInMunicipality } = useMe();

const { data: page, pending } = await getMyAssignmentsInMunicipality(
  municipalityId,
  pagination
);

const { items: assignments, totalElements } = usePagination(pagination, page);

const departments = computed(() => assignments.value.map((a) => a.department));

const { currentAffiliation } = useLoggedInUserStore();
</script>

<template>
  <DepartmentsPage
    v-if="currentAffiliation"
    title="Departments"
    :description="`Enter in one of the departments of ${currentAffiliation.municipality.name}.`"
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <div v-else class="size-full flex flex-col">
      <div class="flex my-4">
        <span class="text-muted text-sm">
          Encontered {{ totalElements }} department(s)
        </span>
      </div>

      <NotFoundMessage v-if="departments.length === 0"
        >No departments found.</NotFoundMessage
      >

      <div
        v-else
        class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4"
      >
        <DepartmentsCard
          v-for="department in departments"
          v-bind="department"
          :key="department.id"
        />
      </div>
    </div>
  </DepartmentsPage>
</template>
