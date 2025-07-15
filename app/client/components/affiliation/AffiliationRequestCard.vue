<script lang="ts" setup>
import type { ChipProps } from "@nuxt/ui";
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Official,
} from "~/types/domain";

const affiliationRequest = defineProps<AffiliationRequest>();

const official: Official = {
  id: "",
  document: "",
  email: "mates@gmail.com",
  name: "Mateus",
  role: "OFFICIAL",
  wasApproved: false,
  createdAt: "",
  updatedAt: "",
};

const requestedAt = Date.parse(affiliationRequest.createdAt);
const formattedRequestedAt = useDateFormat(requestedAt, "DD/MM/YYYY");

const chipColor = computed(() => {
  const status = affiliationRequest.status;

  const colorByStatus: Record<AffiliationRequestStatus, ChipProps["color"]> = {
    PENDING: "warning",
    APPROVED: "success",
    REJECTED: "error",
  };

  return colorByStatus[status];
});

function approve(requestId: string) {}

function reject(requestId: string) {}

function cancel(requestId: string) {}
</script>

<template>
  <UChip :color="chipColor" size="xl">
    <UCard
      class="w-full"
      :variant="status === 'PENDING' ? 'outline' : 'subtle'"
    >
      <div class="w-full flex items-center gap-3">
        <UAvatar :alt="official.name" size="2xl" />
        <div class="flex flex-col">
          <h3 class="font-semibold">{{ official.name }}</h3>
          <div class="text-sm flex flex-col">
            <span
              >E-mail:
              <UBadge variant="soft" color="neutral">{{
                official.email
              }}</UBadge></span
            >
            <span>Requested at {{ formattedRequestedAt }}</span>
          </div>
        </div>
      </div>

      <template #footer>
        <div v-if="status === 'PENDING'" class="w-full grid grid-cols-2 gap-2">
          <UButton
            class="flex justify-center"
            color="error"
            variant="subtle"
            @click="reject(id)"
            >Reject</UButton
          >
          <UButton
            class="flex justify-center"
            color="success"
            variant="subtle"
            @click="approve(id)"
            >Approve</UButton
          >
        </div>
        <div v-else class="w-full flex justify-center items-center">
          <UButton
            class="w-full flex justify-center"
            color="neutral"
            variant="solid"
            @click="cancel(id)"
            >Cancel</UButton
          >
        </div>
      </template>
    </UCard>
  </UChip>
</template>
