<script lang="ts" setup>
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
  Municipality,
} from "~/types/domain";
import type { Badge } from "~/types/ui";

const { t } = useI18n();

const affiliation = defineProps<{
  affiliationRequest: AffiliationRequest;
  municipality: Municipality;
}>();

const formattedRequestedAt = useDateFormat(
  affiliation.affiliationRequest.createdAt,
  "DD/MM/YYYY"
);

const statusBadge = computed<Badge>(() => {
  const status = affiliation.affiliationRequest.status;

  const badgeMap: Record<AffiliationRequestStatus, Badge> = {
    PENDING: { color: "warning", text: status },
    REJECTED: { color: "error", text: status },
    APPROVED: { color: "primary", text: status },
  };

  return badgeMap[status];
});
</script>

<template>
  <UCard>
    <div class="flex items-center gap-3">
      <UAvatar
        :src="affiliation.municipality.iconUrl"
        :alt="affiliation.municipality.name"
        size="3xl"
      />
      <div class="flex flex-col gap-0 lg:gap-1">
        <h3 class="font-semibold text-lg">
          {{ affiliation.municipality.name }}
        </h3>
        <p class="text-sm">
          {{ t("municipalities.status") }}:
          <UBadge :color="statusBadge.color" variant="subtle">
            {{ t(`municipalities.${statusBadge.text.toLowerCase()}`) }}
          </UBadge>
        </p>
        <p class="text-sm">
          {{ t("municipalities.requestedAt", { date: formattedRequestedAt }) }}
        </p>
      </div>
    </div>
  </UCard>
</template>
