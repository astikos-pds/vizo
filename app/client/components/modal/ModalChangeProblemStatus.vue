<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { Problem, ProblemStatus } from "~/types/domain/problem";

const { t } = useI18n();

const { problem } = defineProps<{
  problem: Problem;
}>();

const items = ref<ProblemStatus[]>([
  "ANALYSIS",
  "IN_PROGRESS",
  "REJECTED",
  "RESOLVED",
]);

const changeProblemStatusSchema = z.object({
  newStatus: z.custom<ProblemStatus>(),
});

type ChangeProblemStatusSchema = z.infer<typeof changeProblemStatusSchema>;

const form = reactive<ChangeProblemStatusSchema>({
  newStatus: problem.status,
});

const { currentAssignment } = useLoggedInUserStore();
const { loading, changeProblemStatusInScope } = useDepartments();
const toast = useToast();

const open = ref(false);

const onSubmit = async (event: FormSubmitEvent<ChangeProblemStatusSchema>) => {
  if (
    !currentAssignment ||
    !currentAssignment.effectivePermission.canUpdateStatus
  )
    return;

  const municipalityId = currentAssignment.department.municipality.id;
  const departmentId = currentAssignment.department.id;

  const ok = await changeProblemStatusInScope(
    municipalityId,
    departmentId,
    problem.id,
    {
      status: event.data.newStatus,
    }
  );

  if (!ok) return;

  toast.add({
    title: t('components.modal.problemStatusChanged'),
    description: t('components.modal.problemStatusUpdated'),
    color: "success",
  });

  await refreshNuxtData(
    `municipalities-${municipalityId}-departments-${departmentId}-problems-${problem.id}`
  );
};

const hasUnsavedChanges = computed(() => {
  return form.newStatus !== problem.status;
});
</script>

<template>
  <UModal
    v-model:open="open"
    :title="t('components.modal.updateProblemStatus')"
    :description="t('components.modal.chooseNewStatus')"
    :ui="{ footer: 'justify-end' }"
  >
    <template #body="{ close }">
      <UForm
        :schema="changeProblemStatusSchema"
        :state="form"
        @submit="onSubmit"
        class="w-full flex flex-col items-center gap-5"
      >
        <UFormField :label="t('components.modal.newStatus')" name="status" required class="w-full">
          <USelect :items="items" v-model="form.newStatus" class="w-full" />
        </UFormField>

        <UButton
          v-if="!hasUnsavedChanges"
          color="neutral"
          variant="outline"
          @click="close"
          >{{ t('common.cancel') }}</UButton
        >

        <UButton v-else color="neutral" type="submit" :loading="loading">{{
          t('components.assignedUsers.saveChanges')
        }}</UButton
        >
      </UForm>
    </template>
  </UModal>
</template>
