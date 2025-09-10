<script lang="ts" setup>
import type { ChipProps } from "@nuxt/ui";
import { useAffiliatedUsers } from "~/composables/use-affiliated-users";
import type {
  AffiliatedUser,
  AffiliationStatus,
} from "~/types/domain/affiliated-user";

const { locale } = useI18n();

const affiliatedUser = defineProps<AffiliatedUser>();

const chipColor = computed(() => {
  const status = affiliatedUser.status;

  const colorByStatus: Record<AffiliationStatus, ChipProps["color"]> = {
    PENDING: "warning",
    APPROVED: "success",
    REJECTED: "error",
  };

  return colorByStatus[status];
});

const { changeAffiliationStatus } = useAffiliatedUsers();

async function updateAffiliationStatus(status: AffiliationStatus) {
  const municipalityId = affiliatedUser.municipality.id;
  const affiliationId = affiliatedUser.id;

  await changeAffiliationStatus(municipalityId, affiliationId, { status });

  await refreshNuxtData(`municipality-${municipalityId}-affiliations`);
}

async function approve() {
  await updateAffiliationStatus("APPROVED");
}

async function reject() {
  await updateAffiliationStatus("REJECTED");
}

async function cancel() {
  await updateAffiliationStatus("PENDING");
}
</script>

<template>
  <UChip :color="chipColor" size="xl">
    <UCard
      class="w-full"
      :variant="status === 'PENDING' ? 'outline' : 'subtle'"
    >
      <div class="w-full flex items-center gap-3">
        <UAvatar
          :src="affiliatedUser.user.avatarUrl?.toString()"
          :alt="affiliatedUser.user.name"
          size="2xl"
        />
        <div class="flex flex-col">
          <h3 class="font-semibold">{{ affiliatedUser.user.name }}</h3>
          <div class="text-xs 2xl:text-sm flex flex-col">
            <span
              >E-mail:
              <UBadge variant="subtle" color="neutral" size="sm">{{
                affiliatedUser.institutionalEmail
              }}</UBadge></span
            >
            <span
              >Requested at
              {{ affiliatedUser.affiliatedAt.toLocaleDateString(locale) }}</span
            >
          </div>
        </div>
      </div>

      <template v-if="!affiliatedUser.isAdmin" #footer>
        <div v-if="status === 'PENDING'" class="w-full grid grid-cols-2 gap-2">
          <UButton
            class="flex justify-center"
            color="error"
            variant="subtle"
            @click="reject"
            >Reject</UButton
          >
          <UButton
            class="flex justify-center"
            color="success"
            variant="subtle"
            @click="approve"
            >Approve</UButton
          >
        </div>
        <div v-else class="w-full flex justify-center items-center">
          <UButton
            class="w-full flex justify-center"
            color="neutral"
            variant="solid"
            @click="cancel"
            >Cancel</UButton
          >
        </div>
      </template>
    </UCard>
  </UChip>
</template>
