<script lang="ts" setup>
import DepartmentsCard from "~/components/departments/DepartmentsCard.vue";
import type { Pagination } from "~/types/domain/pagination";

const { t } = useI18n();

useHead({
  title: t("head.departments.title"),
  meta: [
    {
      name: "description",
      content: t("head.departments.description"),
    },
  ],
});

definePageMeta({
  name: "Departments",
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
    :title="t('pages.departments.title')"
    :description="
      t('pages.departments.description', {
        municipalityName: currentAffiliation.municipality.name,
      })
    "
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <div v-else class="size-full flex flex-col">
      <div class="flex my-4">
        <span class="text-muted text-sm">
          {{ t("pages.departments.found", { count: totalElements }) }}
        </span>
      </div>

      <EmptyMessage v-if="departments.length === 0" class="mt-5">
        <span>
          {{ t("pages.departments.noDepartments") }}
          <NuxtLink
            v-if="currentAffiliation.isAdmin"
            :to="`/municipalities/${municipalityId}/departments/new`"
            class="text-primary"
          >
            {{ t("pages.departments.createFirstOne") }}
          </NuxtLink>
        </span>
      </EmptyMessage>

      <div
        v-else
        class="w-full grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4"
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
