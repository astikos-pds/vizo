<script setup lang="ts">
import type { Pagination } from "~/types/domain/pagination";

const { t } = useI18n();

useHead({
  title: t("head.problems.title"),
  meta: [
    {
      name: "description",
      content: t("head.problems.description"),
    },
  ],
});

definePageMeta({
  name: "Problems",
  middleware: ["auth", "assigned"],
});

const { currentAssignment } = useLoggedInUserStore();

const { getProblemsInScope } = useDepartments();

const pagination = ref<Pagination>({
  page: 0,
  size: 20,
});

const { data: problems } = await getProblemsInScope(
  currentAssignment ? currentAssignment.department.municipality.id : "",
  currentAssignment ? currentAssignment.department.id : "",
  pagination.value
);
</script>

<template>
  <CommonPage title="Problemas" with-padding>
    <ProblemsTable
      v-if="problems"
      v-model:problems="problems"
      v-model:pagination="pagination"
      class="w-full"
    />
  </CommonPage>
</template>
