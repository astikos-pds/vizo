<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";

const { t } = useI18n();

useHead({
  title: t("head.assignUsers.title"),
  meta: [
    {
      name: "description",
      content: t("head.assignUsers.description"),
    },
  ],
});

definePageMeta({
  name: "Assign a filiate",
  middleware: ["auth", "assigned"],
});

const route = useRoute();
const departmentId = route.params.departmentId as string;

const { currentAssignment } = useLoggedInUserStore();
const municipalityId = currentAssignment
  ? currentAssignment.department.municipality.id
  : "";

const { loading, getUsersAssignedToDepartment, assignUsersToDepartment } =
  useAssignedUsers();

const { data: usersAlreadyAssigned } = await getUsersAssignedToDepartment(
  municipalityId,
  departmentId,
  {
    page: 0,
    size: 100,
  }
);

const filiatesAlreadyAssigned = computed(() =>
  usersAlreadyAssigned.value
    ? usersAlreadyAssigned.value.content.map((a) => a.user)
    : []
);

const assignSchema = z.object({
  assignedUsers: z.array(z.custom<AffiliatedUser>()),
});

type AssignSchema = z.infer<typeof assignSchema>;

const form = reactive<AssignSchema>({
  assignedUsers: filiatesAlreadyAssigned.value,
});

const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<AssignSchema>) => {
  const usersToAssign = event.data.assignedUsers;

  const response = await assignUsersToDepartment(municipalityId, departmentId, {
    affiliationsIds: usersToAssign.map((a) => a.id),
  });

  if (!response) return;

  toast.add({
    title: t("pages.assignUsers.success.title"),
    description: t("pages.assignUsers.success.description"),
    color: "success",
  });

  await refreshNuxtData(
    `municipalities-${municipalityId}-departments-${departmentId}-assignments`
  );

  await navigateTo(`/departments/${departmentId}/assignees`);
};
</script>

<template>
  <AssignedUsersPage v-if="currentAssignment">
    <div class="size-full flex justify-center">
      <div class="w-[70%] mt-15 flex flex-col gap-5">
        <header class="w-full flex flex-col items-center gap-1">
          <h1 class="text-2xl font-semibold">
            {{
              t("pages.assignUsers.title", {
                departmentName: currentAssignment.department.name,
              })
            }}
          </h1>
          <span class="text-sm">{{ t("pages.assignUsers.description") }}</span>
        </header>
        <main class="w-full flex flex-col items-center">
          <UForm
            :schema="assignSchema"
            :state="form"
            class="w-full flex flex-col items-center gap-5"
          >
            <UFormField
              :label="t('pages.assignUsers.usersAssigned')"
              name="users-assigned"
              class="w-full"
            >
              <MunicipalitiesFiliatesPalette
                v-model="form.assignedUsers"
                class="border border-default rounded-md"
              />
            </UFormField>

            <UButton type="submit" :loading="loading">{{
              t("pages.assignUsers.assign")
            }}</UButton>
          </UForm>
        </main>
      </div>
    </div>
  </AssignedUsersPage>
</template>
