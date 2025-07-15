<script lang="ts" setup>
import type { Department, Municipality } from "~/types/domain";

definePageMeta({
  layout: "official",
  // middleware: ["auth", "official"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

// const municipality = useNuxtData<Municipality>(`municipality-${municipalityId}`)
const municipality: Municipality = {
  id: municipalityId,
  name: "São Paulo",
  emailDomain: "",
  iconUrl: "",
  createdAt: "",
  updatedAt: "",
};

const departments = ref<Department[]>([
  {
    id: "1",
    municipalityId: municipality.id,
    name: "Tapa-buraco",
    iconUrl: "",
    colorHex: "#000000",
    createdById: "",
    createdAt: "2024-09-01",
    updatedAt: "",
  },
  {
    id: "1",
    municipalityId: municipality.id,
    name: "Tapa-buraco",
    iconUrl: "",
    colorHex: "#000000",
    createdById: "",
    createdAt: "2024-09-01",
    updatedAt: "",
  },
  {
    id: "1",
    municipalityId: municipality.id,
    name: "Tapa-buraco",
    iconUrl: "",
    colorHex: "#000000",
    createdById: "",
    createdAt: "2024-09-01",
    updatedAt: "",
  },
]);

const newDepartmentLink = `/municipalities/${municipalityId}/departments/new`;
</script>

<template>
  <OfficialPage
    title="Departments"
    :description="`Enter in one of the departments of ${municipality.name}.`"
  >
    <div class="size-full flex flex-col">
      <div class="flex my-4 justify-between items-center">
        <span class="text-muted">
          Encontered {{ departments.length }} department(s)
        </span>

        <UButton icon="i-lucide-plus" :to="newDepartmentLink"
          >New department</UButton
        >
      </div>

      <div class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <DepartmentCard
          v-for="department in departments"
          v-bind="department"
          :key="department.id"
        />
      </div>
    </div>
  </OfficialPage>
</template>
