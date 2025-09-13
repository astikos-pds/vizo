<script lang="ts" setup>
import type { AssignedUser } from "~/types/domain/assigned-user";
import type { Pagination } from "~/types/domain/pagination";

useHead({
  title: "Vizo | List assigned users",
  meta: [
    {
      name: "description",
      content: "View all assigned users in this department",
    },
  ],
});

definePageMeta({
  name: "Assigned users",
  middleware: ["auth", "assigned"],
});

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

const { getPermissionPresetsInMunicipality } = usePermissionPresets();

const { data: permissionPresets, pending: pendingForPresets } =
  await getPermissionPresetsInMunicipality(
    currentAffiliation ? currentAffiliation.municipality.id : ""
  );

const {
  items: assignedUsers,
  currentPage,
  totalElements,
} = usePagination(pagination, page);

const selectedAssignee = ref<AssignedUser | null>(null);
</script>

<template>
  <AssignedUsersPage>
    <div class="size-full flex">
      <aside class="w-[30%] border-r border-default">
        <section
          class="size-full flex flex-col justify-between overflow-y-auto"
        >
          <header class="p-3 text-center border-b border-default">
            <h1 class="text-xl font-semibold">Assignees</h1>
          </header>
          <main class="flex-1 flex flex-col p-3">
            <EmptyMessage v-if="pendingForUsers">Loading...</EmptyMessage>
            <EmptyMessage v-else-if="!page"
              >Failed to fetch assignees</EmptyMessage
            >
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
            class="p-2 2xl:p-3 flex justify-center items-center border-t border-default"
          >
            <UPagination
              v-model:page="currentPage"
              :items-per-page="pagination.size"
              :total="totalElements"
            />
          </footer>
        </section>
      </aside>
      <div class="flex-1">
        <div class="size-full flex justify-center items-center">
          <UIcon
            v-if="!selectedAssignee"
            name="lucide:contact"
            mode="svg"
            class="size-30"
          />
          <EmptyMessage v-else-if="pendingForPresets">Loading...</EmptyMessage>
          <EmptyMessage v-else-if="permissionPresets === null"
            >Failed to fetch permission presets in this
            municipality</EmptyMessage
          >
          <AssignedUsersDetailsCard
            v-else
            :assigned-user="selectedAssignee"
            :permission-presets="permissionPresets"
            @close="() => (selectedAssignee = null)"
            class="w-[80%]"
          />
        </div>
      </div>
    </div>
  </AssignedUsersPage>
</template>
