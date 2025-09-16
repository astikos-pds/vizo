<script lang="ts" setup>
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { Pagination } from "~/types/domain/pagination";

const selectedAssignee = defineModel<AssignedUser>();

const route = useRoute();
const departmentId = route.params.departmentId as string;

const { currentAffiliation } = useLoggedInUserStore();

const pagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { getUsersAssignedToDepartment } = useAssignedUsers();

const { data: page, pending: pendingForUsers } =
  await getUsersAssignedToDepartment(
    currentAffiliation ? currentAffiliation.municipality.id : "",
    departmentId,
    pagination
  );

const {
  items: assignedUsers,
  currentPage,
  totalElements,
} = usePagination(pagination, page);
</script>

<template>
  <section class="size-full flex flex-col justify-between overflow-y-auto">
    <main class="flex-1 flex flex-col p-3 2xl:p-5">
      <EmptyMessage v-if="pendingForUsers">Loading...</EmptyMessage>
      <EmptyMessage v-else-if="!page">Failed to fetch assignees</EmptyMessage>
      <AssignedUsersCard
        v-else
        v-for="assignedUser in assignedUsers"
        :assigned-user="assignedUser"
        :selected="selectedAssignee?.id === assignedUser.id"
        :key="assignedUser.id"
        @click="(a) => (selectedAssignee = a)"
      />
    </main>
    <footer
      v-if="page && page.totalPages > 1"
      class="p-2 2xl:p-3 flex justify-center items-center border-t border-default"
    >
      <UPagination
        v-model:page="currentPage"
        :items-per-page="pagination.size"
        :total="totalElements"
      />
    </footer>
  </section>
</template>
